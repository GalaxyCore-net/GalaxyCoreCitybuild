package net.galaxycore.citybuild.pmenu.menu.help;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.menu.PMenuHelpMenu;
import net.galaxycore.citybuild.utils.StringUtils;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class ClaimMenu extends Menu {

    private final Player player;

    public ClaimMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        registerCommandI18N("buy",
                "Buy the plot you are standing on",
                "Kaufe das Grundstück auf dem du stehst");
        registerCommandI18N("claim",
                "Claim the current plot you are standing on",
                "Hol' dir das Grundstück auf dem du stehst");
        registerCommandI18N("auto",
                "Claim the nearest plot",
                "Hol' dir das nächste Grundstück");
        registerCommandI18N("delete",
                "Delete the plot you stand on",
                "Lösche das Grundstück auf dem du stehst");
        registerCommandI18N("swap",
                "Swap two plots",
                "Tausche zwei Grundstücke");
        registerCommandI18N("move",
                "Move a plot",
                "Bewege ein Grundstück");
        registerCommandI18N("copy",
                "Copy a plot",
                "Kopiere ein Grundstück");
        registerCommandI18N("grant",
                "Manage plot grants",
                "Verwalte Grundstücksberechtigungen");
    }

    @Override
    public String getMenuName() {
        return StringUtils.firstLetterUppercase(i18n("claiming"));
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

    //claiming: buy claim auto delete swap move copy grant
    @Override
    public void setMenuItems() {

        addMenuItem(3, i18n("buy"), getDescription("buy"));
        addMenuItem(4, i18n("claim"), getDescription("claim"));
        addMenuItem(5, i18n("auto"), getDescription("auto"));
        addMenuItem(9+3, i18n("delete"), getDescription("delete"));
        addMenuItem(9+5, i18n("swap"), getDescription("swap"));
        addMenuItem(18+3, i18n("move"), getDescription("move"));
        addMenuItem(18+4, i18n("copy"), getDescription("copy"));
        addMenuItem(18+5, i18n("grant"), getDescription("grant"));
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
        PMenuHelpMenu.addHelpItem(getInventory(), slot, Material.BEACON, command, description);
    }

}
