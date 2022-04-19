package net.galaxycore.citybuild.shop

import lombok.Getter
import me.kodysimpson.menumanagersystem.menusystem.Menu
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@Getter
class ShopGUI(player: Player, private val shop: Shop) : Menu(PlayerMenuUtility.getPlayerMenuUtility(player)) {
    override fun getMenuName(): String = "shit shop test menu with " + shop.len + " items"

    override fun getSlots(): Int = 6 * 9

    override fun handleMenu(inventoryClickEvent: InventoryClickEvent) {}
    override fun setMenuItems() {
        inventory.addItem(makeItem(Material.TOTEM_OF_UNDYING, "Price: " + shop.price))
        inventory.addItem(ItemStack.deserialize(shop.itemStack))
    }
}