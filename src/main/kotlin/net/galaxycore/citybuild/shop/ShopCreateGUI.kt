package net.galaxycore.citybuild.shop

import net.galaxycore.citybuild.utils.Skull
import net.galaxycore.galaxycorecore.spice.KMenu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class ShopCreateGUI (private val player: Player) : KMenu() {
    private lateinit var callback: (ShopCreateGUI) -> Unit;
    private val priceItem: KMenu.KMenuItem
    private var price = 0

    override fun getSize() = 5*9
    override fun getNameI18NKey() ="Shop create GUI lol :D"

    init {
        item(20, ItemStack(Material.AIR))
        priceItem = item(24, ItemStack(Material.SUNFLOWER, price))

        item(23, Skull.ARROW_UP.skull).then {
            priceItem.itemStack.update {
                price++
                it.amount = price
                it
            }
        }
        item(25, Skull.ARROW_DOWN.skull).then {
            priceItem.itemStack.update {
                price--
                if (price < 0) price = 0
                it.amount = price
                it
            }
        }
    }

    fun open(callback: (ShopCreateGUI) -> Unit) {
        this.callback = callback
        open(player)
    }

//    override fun handleMenu(p0: InventoryClickEvent) {
//        if (p0.currentItem == null) return
//
//        val rawSlot = p0.rawSlot
//
//        if (rawSlot == 0) {
//            playerMenuUtility.owner.closeInventory(InventoryCloseEvent.Reason.PLUGIN)
//            callback(this)
//        }
//    }
//
//    override fun setMenuItems() {
//        setFillerGlass()
//        inventory.setItem(20, ItemStack(Material.AIR))
//        inventory.setItem(23, Skull.ARROW_UP.skull)
//        inventory.setItem(24, ItemStack(Material.SUNFLOWER, price))
//        inventory.setItem(25, Skull.ARROW_DOWN.skull)
//    }
}