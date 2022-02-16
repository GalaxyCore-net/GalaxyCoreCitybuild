package net.galaxycore.citybuild.pmenu.menu.help;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.menu.PMenuHelpMenu;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class TeleportMenu extends Menu {

    private final Player player;

    public TeleportMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        registerCommandI18N("home",
                "Teleport to your plot(s)",
                "Teleportiere dich zu deinem/n Grundstück(en)");
        registerCommandI18N("visit",
                "Visit someones Plot",
                "Schaue dir das Grundstück von jemandem an");
        registerCommandI18N("kick",
                "Kick a player from your plot",
                "Kicke einen Spieler aus deinem Plot");
        registerCommandI18N("middle",
                "Teleports you to the center of the plot",
                "Teleportiert dich zu dem Zentrum des Grundstücks");
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

        if(inventoryClickEvent.getRawSlot() < 3*9) {
            if(Objects.requireNonNull(inventoryClickEvent.getCurrentItem()).getType().equals(Material.EMERALD)) {
                new PMenuHelpMenu(player).open();
            }
        }

    }

    //teleport: home visit kick middle
    @Override
    public void setMenuItems() {

        addMenuItem(3, i18n("home"), getDescription("home"));
        addMenuItem(5, i18n("visit"), getDescription("visit"));
        addMenuItem(18+3, i18n("kick"), getDescription("kick"));
        addMenuItem(18+5, i18n("middle"), getDescription("middle"));
        inventory.setItem(9+4, makeItem(Material.EMERALD, i18n("help")));

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

    private String[] getDescription(String command) {
        return i18n(command + "_desc").split("\n");
    }

    private void registerCommandI18N(String command, String commandDescEN, String commandDescDE) {
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help." + command, "§6" + command);
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help." + command + "_desc", "§7" + commandDescEN);
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help." + command, "§6" + command);
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help." + command + "_desc", "§7" + commandDescDE);
    }

    private void addMenuItem(Material material, String command, String... description) {
        PMenuHelpMenu.addHelpItem(getInventory(), material, command, description);
    }

    private void addMenuItem(int slot, String command, String... description) {
        PMenuHelpMenu.addHelpItem(getInventory(), slot, Material.ENDER_PEARL, command, description);
    }


}
