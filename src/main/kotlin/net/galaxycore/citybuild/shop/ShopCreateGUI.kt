package net.galaxycore.citybuild.shop

import net.galaxycore.citybuild.utils.Skull
import net.galaxycore.galaxycorecore.spice.KMenu
import net.galaxycore.galaxycorecore.spice.reactive.Reactive
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class ShopCreateGUI (private val player: Player, itemStack: ItemStack) : KMenu() {
    private lateinit var callback: (ShopCreateGUI) -> Unit
    private val up: KMenu.KMenuItem = item(13, Skull.ARROW_UP.skull)
    private val down: KMenu.KMenuItem = item(31, Skull.ARROW_DOWN.skull)
    var price: Long = 1
    var state: Reactive<Int> = Reactive(Shop.STATE_BUY)

    override fun getSize() = 5*9
    override fun getNameI18NKey() ="Shop create GUI lol :D"

    init {
        item(22, itemStack)
        up.then {
            val change = if (it.clickType.isRightClick) 10 else 1
            price += change
            updatePriceOn(this.up)
            updatePriceOn(this.down)
        }
        down.then {
            val change = if (it.clickType.isRightClick) 10 else 1
            price -= change
            if (price < 0) price = 0

            updatePriceOn(this.up)
            updatePriceOn(this.down)
        }

        item(28, Material.LIME_STAINED_GLASS, "§a§lCreate shop").then {
            player.closeInventory()
            callback(this)
        }

        item(34, Material.RED_STAINED_GLASS, "§c§lCancel").then {
            player.closeInventory()
        }

        val buyState = item(6, Material.SUNFLOWER, "§a§lBuy").then {
            state.setItem(Shop.STATE_BUY)
        }

        val sellState = item(7, Material.ARROW, "§a§lSell").then {
            state.setItem(Shop.STATE_SELL)
        }

        val bothState = item(8, Material.GOLD_INGOT, "§a§lBuy and Sell").then {
            state.setItem(Shop.STATE_BTH)
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

    private fun updatePriceOn(item: KMenu.KMenuItem) {
        item.itemStack.update {
            val meta = it.itemMeta
            meta.displayName(buildPriceComponent())
            it.itemMeta = meta
            it
        }
    }


    private fun buildPriceComponent() = Component.text("Price: $price Coins")

    fun open(callback: (ShopCreateGUI) -> Unit) {
        this.callback = callback
        open(player)

        updatePriceOn(this.up)
        updatePriceOn(this.down)
        state.setItem(Shop.STATE_BUY)
    }

    companion object {
        fun enchantIfStateMatch(itemStack: ItemStack, state: Int, baseState: Int): ItemStack {
            itemStack.itemMeta = itemStack.itemMeta?.apply {
                this.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
            if (baseState == state) {
                itemStack.itemMeta = itemStack.itemMeta?.apply {
                    this.addEnchant(Enchantment.DURABILITY, 1, true)
                }
            }
            else {
                itemStack.removeEnchantment(Enchantment.DURABILITY)
            }
            return itemStack
        }
    }
}