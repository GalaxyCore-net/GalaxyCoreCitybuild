package net.galaxycore.citybuild.shop

import net.galaxycore.citybuild.shop.ShopCreateGUI.Companion.enchantIfStateMatch
import net.galaxycore.galaxycorecore.spice.KMenu
import net.galaxycore.galaxycorecore.spice.reactive.Reactive
import org.bukkit.Material
import org.bukkit.entity.Player

class ShopEditGUI(player: Player, r: Shop) : KMenu(){
    private val state = Reactive(r.state)

    init {
        val buyState = item(6, Material.SUNFLOWER, "§a§lBuy").then {
            state.setItem(Shop.STATE_BUY)
        }

        val sellState = item(7, Material.ARROW, "§a§lSell").then {
            state.setItem(Shop.STATE_SELL)
        }

        val bothState = item(8, Material.GOLD_INGOT, "§a§lBuy and Sell").then {
            state.setItem(Shop.STATE_BTH)
        }
        item(11, Material.STONE_BUTTON, "Refill")
        item(13, r.itemStack)
        item(15, Material.IRON_AXE, "Change Price")
        item(18, Material.TNT, "Adminshop")


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


    override fun getNameI18NKey() = "shop.edit.title"
    override fun getSize() = 3*9
}
