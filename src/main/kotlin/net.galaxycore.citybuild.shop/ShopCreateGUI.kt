package net.galaxycore.citybuild.shop

import me.kodysimpson.menumanagersystem.menusystem.Menu
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class ShopCreateGUI (player: Player): Menu(PlayerMenuUtility.getPlayerMenuUtility(player)) {
    override fun getMenuName() = "ShopCreateGUI lol"

    override fun getSlots(): Int = 9

    override fun handleMenu(p0: InventoryClickEvent?) {
    }

    override fun setMenuItems() {
    }
}