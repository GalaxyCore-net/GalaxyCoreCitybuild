package net.galaxycore.citybuild.pmenu.menu.help;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DebugMenu extends Menu {

    private final Player player;

    public DebugMenu(Player player) {
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

    }

    //debug: debugallowunsafe debug debugpaste debugroadregen
    @Override
    public void setMenuItems() {

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

}
