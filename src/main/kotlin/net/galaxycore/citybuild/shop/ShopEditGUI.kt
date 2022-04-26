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

class ShopEditGUI(player: Player, r: Shop, private val block: Block) : KMenu() {
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
        item(11, Material.STONE_BUTTON, ShopI18N.get<ShopEditGUI>(player, "refill"), ShopI18N.get<ShopEditGUI>(player, "current").replace("%d", r.len.toString())).then {
            ShopRefillGUI(player, r, block).open()
        }
        item(13, r.itemStack)
        item(15, Material.IRON_AXE, ShopI18N.get<ShopEditGUI>(player, "changeprice"))
        if (player.hasPermission("citybuild.shop.adminshop") && r.player != 0) {
            item(18, Material.TNT, ShopI18N.get<ShopEditGUI>(player, "makeadminshop")).then {
                val shopData = KBlockData(block, Essential.getInstance())
                val shop = Shop.disect(shopData)
                shop.player = 0
                shop.compact(shopData)
                player.inventory.close()
            }
        }


        state.updatelistener { item ->
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
        val shop = Shop.disect(shopData)
        shop.state = state.value
        shop.compact(shopData)
    }


    override fun getNameI18NKey() = "citybuild.shop.ShopEditGUI.title"
    override fun getSize() = 3 * 9
}
