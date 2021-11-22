package net.galaxycore.citybuild.pmenu.menu;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PMenuBaseMenu extends Menu {
    private final Player player;

    public PMenuBaseMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.TITLE_MAIN.get(player);
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
    }
}
