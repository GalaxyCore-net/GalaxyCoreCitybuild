package net.galaxycore.citybuild.pmenu.menu;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import org.bukkit.Material;
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
        switch (inventoryClickEvent.getSlot()) {
            case 1:
                openWarp();
                break;
            case 4:
                openPlots();
                break;
            case 7:
                openThisPlot();
                break;
        }
    }

    private void openWarp() {
        player.closeInventory();

        new PMenuWarpMenu(player).open();
    }

    private void openPlots() {
        player.closeInventory();
    }

    private void openThisPlot() {
        player.closeInventory();
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();

        inventory.setItem(1, makeItem(Material.ENDER_EYE, PMenuI18N.MAIN_WARPS.get(player)));
        inventory.setItem(4, makeItem(Material.GRASS_BLOCK, PMenuI18N.MAIN_PLOTS.get(player)));
        inventory.setItem(7, makeItem(Material.NETHER_STAR, PMenuI18N.MAIN_THIS.get(player)));
    }
}