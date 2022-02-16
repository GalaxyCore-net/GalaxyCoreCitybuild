package net.galaxycore.citybuild.pmenu.menu;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.menu.help.*;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PMenuHelpMenu extends Menu {

    private final Player player;

    public PMenuHelpMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
    }

    @Override
    public String getMenuName() {
        return i18n("title");
    }

    @Override
    public int getSlots() {
        return 3*9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

        switch (inventoryClickEvent.getRawSlot()) {
            case 9 -> new ClaimMenu(player).open();
            case 10 -> new TeleportMenu(player).open();
            case 11 -> new SettingsMenu(player).open();
            case 12 -> new ChatMenu(player).open();
            case 13 -> new SchematicMenu(player).open();
            case 14 -> new AppereanceMenu(player).open();
            case 15 -> new InfoMenu(player).open();
            case 16 -> new DebugMenu(player).open();
            case 17 -> new AdministrationMenu(player).open();
            default -> {
            }
        }

    }

    @Override
    public void setMenuItems() {

        inventory.setItem(9, makeItem(Material.GRASS_BLOCK, i18n("claiming")));
        inventory.setItem(10, makeItem(Material.GRASS_BLOCK, i18n("teleport")));
        inventory.setItem(11, makeItem(Material.GRASS_BLOCK, i18n("settings")));
        inventory.setItem(12, makeItem(Material.GRASS_BLOCK, i18n("chat")));
        inventory.setItem(13, makeItem(Material.GRASS_BLOCK, i18n("schematic")));
        inventory.setItem(14, makeItem(Material.GRASS_BLOCK, i18n("appereance")));
        inventory.setItem(15, makeItem(Material.GRASS_BLOCK, i18n("info")));
        inventory.setItem(16, makeItem(Material.GRASS_BLOCK, i18n("debug")));
        inventory.setItem(17, makeItem(Material.GRASS_BLOCK, i18n("administration")));

    }

    private String i18n(String key) {
        String i18n = I18N.getByPlayer(player, "citybuild.pmenu.help." + key);
        if(i18n == null) {
            i18n = I18N.getByLang("en_GB", "citybuild.pmenu.help." + key);
        }
        if(i18n == null) {
            i18n = "§c§lERROR";
        }
        return i18n;
    }

    public static void addHelpItem(Inventory inventory, Material material, String command, String... description) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text(command));
        itemMeta.setLore(Arrays.asList(description));
        item.setItemMeta(itemMeta);
        inventory.addItem(item);
    }

    public static void addHelpItem(Inventory inventory, int slot, Material material, String command, String... description) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text(command));
        itemMeta.setLore(Arrays.asList(description));
        item.setItemMeta(itemMeta);
        inventory.setItem(slot, item);
    }

}
