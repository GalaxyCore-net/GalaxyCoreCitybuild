package net.galaxycore.citybuild.pmenu.menu.help;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.menu.PMenuHelpMenu;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SettingsMenu extends Menu {

    private final Player player;

    public SettingsMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        registerCommandI18N("trust",
                """
                        Allow a user to build\s
                        in a plot and use WorldEdit\s
                        while the plot owner is offline""",
                """
                        Erlaube einem Spieler auf einem Grundstück
                        zu bauen und WorldEdit zu benutzen
                        während der Ploteigentümer offline ist""");
        registerCommandI18N("add",
                """
                        Allow a user to build in a plot \n
                        while the plot owner is online""",
                """
                        Erlaube einem Spieler, auf einem Grundstück zu bauen, \n
                        während der Eigentümer online ist""");
        registerCommandI18N("deny",
                "Deny a user from entering a plot",
                "Verbiete einem Spieler, ein Grundstück zu betreten");
        registerCommandI18N("remove",
                "Remove a user flom a plot",
                "Entferne einen Spieler von einem Grundstück");
        registerCommandI18N("unlink",
                "Unlink a mega-plot",
                "Trenne eine Verbindung eines Mega-Grundstücks");
        registerCommandI18N("flag",
                "Manage plot flags",
                "Verwalte Grundstückseigenschaften");
        registerCommandI18N("done",
                """
                        Continue a plot that was previously \n
                        marked as done""",
                """
                        Fahre fort mit einem Grundstück, \n
                        welches vorher als fertig markiert wurde""");
        registerCommandI18N("continue",
                """
                        Continue a plot that was previously \n
                        marked as done""",
                """
                        Fahre fort mit einem Grundstück, \n
                        welches vorher als fertig markiert wurde""");
        registerCommandI18N("alias",
                "Set the plot alias",
                "Setzte einen Spitznamen für das Grundstück");
        registerCommandI18N("sethome",
                "Set the plot home",
                "Setze das Grundstückszuhause");
        registerCommandI18N("backup",
                "Manage plot backups",
                "Verwalte Plot-Backups");
        registerCommandI18N("toggle",
                "Toggle per user settings",
                "Ändere Einstellungen pro Spieler");
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

    }

    //settings: trust add deny remove unlink flag done continue alias sethome backup toggle
    @Override
    public void setMenuItems() {

        addMenuItem(2, i18n("trust"), getDescription("trust"));
        addMenuItem(3, i18n("add"), getDescription("add"));
        addMenuItem(4, i18n("deny"), getDescription("deny"));
        addMenuItem(5, i18n("remove"), getDescription("remove"));
        addMenuItem(6, i18n("unlink"), getDescription("unlink"));
        addMenuItem(9+3, i18n("flag"), getDescription("flag"));
        addMenuItem(9+5, i18n("done"), getDescription("done"));
        addMenuItem(18+2, i18n("continue"), getDescription("continue"));
        addMenuItem(18+3, i18n("alias"), getDescription("alias"));
        addMenuItem(18+4, i18n("sethome"), getDescription("sethome"));
        addMenuItem(18+5, i18n("backup"), getDescription("backup"));
        addMenuItem(18+6, i18n("toggle"), getDescription("toggle"));
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
        PMenuHelpMenu.addHelpItem(getInventory(), slot, Material.COMMAND_BLOCK, command, description);
    }

}
