package net.galaxycore.citybuild.shop

import net.galaxycore.citybuild.utils.Skull
import net.galaxycore.galaxycorecore.spice.KMenu
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShopCreateGUI (private val player: Player, itemStack: ItemStack) : KMenu() {
    private lateinit var callback: (ShopCreateGUI) -> Unit
    private val priceItem: KMenu.KMenuItem = item(22, itemStack)

    private val up: KMenu.KMenuItem = item(13, Skull.ARROW_UP.skull)
    private val down: KMenu.KMenuItem = item(31, Skull.ARROW_DOWN.skull)
    var price: Long = 1

    override fun getSize() = 5*9
    override fun getNameI18NKey() ="Shop create GUI lol :D"

    init {
        up.then {
            val change = if (it.clickType.isRightClick) 10 else 1
            price += change
            updatePriceOn(this.up)
            updatePriceOn(this.down)
            updatePriceOn(this.priceItem)
        }
        down.then {
            val change = if (it.clickType.isRightClick) 10 else 1
            price -= change
            if (price < 0) price = 0

            updatePriceOn(this.up)
            updatePriceOn(this.down)
            updatePriceOn(this.priceItem)
        }

        item(28, Material.LIME_STAINED_GLASS, "§a§lCreate shop").then {
            player.closeInventory()
            callback(this)
        }

        item(34, Material.RED_STAINED_GLASS, "§c§lCancel").then {
            player.closeInventory()
        }

        updatePriceOn(this.up)
        updatePriceOn(this.down)
        updatePriceOn(this.priceItem)
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
    }
}