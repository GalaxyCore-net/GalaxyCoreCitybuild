package net.galaxycore.citybuild.shop

import net.galaxycore.citybuild.Essential
import net.galaxycore.citybuild.shop.ShopCreateGUI.Companion.enchantIfStateMatch
import net.galaxycore.galaxycorecore.spice.KBlockData
import net.galaxycore.galaxycorecore.spice.KMenu
import net.galaxycore.galaxycorecore.spice.reactive.Reactive
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.persistence.PersistentDataType

class ShopEditGUI(player: Player, r: Shop, private val block: Block) : KMenu(){
    private val state = Reactive(r.state)

    init {
        val buyState = item(6, Material.SUNFLOWER, ShopI18N.get<ShopEditGUI>(player, "buy")).then {
            state.setItem(Shop.STATE_BUY)
        }

        val sellState = item(7, Material.ARROW, ShopI18N.get<ShopEditGUI>(player, "sell")).then {
            state.setItem(Shop.STATE_SELL)
        }

        val bothState = item(8, Material.GOLD_INGOT, ShopI18N.get<ShopEditGUI>(player, "buyandsell")).then {
            state.setItem(Shop.STATE_BTH)
        }
        item(11, Material.STONE_BUTTON, ShopI18N.get<ShopEditGUI>(player, "refill"))
        item(13, r.itemStack)
        item(15, Material.IRON_AXE, ShopI18N.get<ShopEditGUI>(player, "changeprice"))
        item(18, Material.TNT, ShopI18N.get<ShopEditGUI>(player, "makeadminshop")).then {
            val shopData = KBlockData(block, Essential.getInstance())
            val shop = shopData[ShopListener.namespacedKey, PersistentDataType.BYTE_ARRAY]?.let { Shop.disect(it) }

            shop!!.player = -1

            shopData[ShopListener.namespacedKey, PersistentDataType.BYTE_ARRAY] = shop.compact()
            player.inventory.close()
        }


        state.updatelistener {item ->
            buyState.itemStack.update {
                enchantIfStateMatch(it, Shop.STATE_BUY, item)
            }
            sellState.itemStack.update {
                enchantIfStateMatch(it, Shop.STATE_SELL, item)
            }
            bothState.itemStack.update {
                enchantIfStateMatch(it, Shop.STATE_BTH, item)
            }
        }

    }

    override fun onclose(inventoryCloseEvent: InventoryCloseEvent) {
        val shopData = KBlockData(block, Essential.getInstance())
        val shop = shopData[ShopListener.namespacedKey, PersistentDataType.BYTE_ARRAY]?.let { Shop.disect(it) }

        shop!!.state = state.value

        shopData[ShopListener.namespacedKey, PersistentDataType.BYTE_ARRAY] = shop.compact()
    }


    override fun getNameI18NKey() = "galaxycore.citybuild.shop.ShopEditGUI.title"
    override fun getSize() = 3*9
}
