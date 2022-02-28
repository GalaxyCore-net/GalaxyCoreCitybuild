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
        return 9 * 5;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        switch (inventoryClickEvent.getRawSlot()) {
            case 11 -> openWarp();
            case 22 -> openPlots();
            case 15 -> openThisPlot();
            case 29 -> openHelp();
        }
    }

    private void openWarp() {
        player.closeInventory();

        new PMenuWarpMenu(player).open();
    }

    private void openPlots() {
        player.closeInventory();
        new PMenuPlotsMenu(player).open();
    }

    private void openThisPlot() {
        player.closeInventory();
        new PMenuPlotInfoMenu(player, null).open();
    }

    private void openHelp() {
        player.closeInventory();
        new PMenuHelpMenu(player).open();
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();

        inventory.setItem(11, makeItem(Material.ENDER_EYE, PMenuI18N.MAIN_WARPS.get(player)));
        inventory.setItem(22, makeItem(Material.BOOK, PMenuI18N.MAIN_PLOTS.get(player)));
        inventory.setItem(15, makeItem(Material.NETHER_STAR, PMenuI18N.MAIN_THIS.get(player)));
        inventory.setItem(4, makeItem(Material.FLOWER_BANNER_PATTERN, PMenuI18N.MAIN_LICENCES.get(player)));
        inventory.setItem(40, makeItem(Material.HEART_OF_THE_SEA, PMenuI18N.MAIN_PLOT_VISIT.get(player)));
        inventory.setItem(29, makeItem(Material.EMERALD, PMenuI18N.MAIN_HELP.get(player)));
        inventory.setItem(33, makeItem(Material.GOLD_INGOT, PMenuI18N.MAIN_PLOT_BUY.get(player)));
    }
}