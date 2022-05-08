package net.galaxycore.citybuild.shop

import net.galaxycore.citybuild.Essential
import net.galaxycore.citybuild.utils.Skull
import net.galaxycore.galaxycorecore.spice.KBlockData
import net.galaxycore.galaxycorecore.spice.KMenu
import net.kyori.adventure.text.Component
import org.bukkit.block.Block
import org.bukkit.entity.Player

class ShopPriceGUI (private val player: Player, private val shop: Shop, private val block: Block) : KMenu() {
    private val up: KMenu.KMenuItem = item(13, Skull.ARROW_UP.skull)
    private val down: KMenu.KMenuItem = item(31, Skull.ARROW_DOWN.skull)
    private var price: Long = 1

    override fun getSize() = 5*9
    override fun getNameI18NKey() ="citybuild.shop.ShopCreateGUI.title"

    init {
        item(22, shop.itemStack)
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
    }

    private fun updatePriceOn(item: KMenu.KMenuItem) {
        item.itemStack.update {
            val meta = it.itemMeta
            meta.displayName(buildPriceComponent())
            it.itemMeta = meta
            it
        }

        shop.price = price
        shop.compact(KBlockData(block, Essential.getInstance()))
    }


    private fun buildPriceComponent() = Component.text(ShopI18N.get<ShopPriceGUI>(player, "price").replace("%d", price.toString()))

    fun open() {
        open(player)

        updatePriceOn(this.up)
        updatePriceOn(this.down)
    }

}