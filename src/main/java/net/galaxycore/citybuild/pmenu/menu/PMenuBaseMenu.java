package net.galaxycore.citybuild.pmenu.menu;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.apiutils.CoreProvider;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
            case 4 -> openLizenz();
            case 11 -> openWarp();
            case 22 -> openPlots();
            case 15 -> openThisPlot();
            case 33 -> openPlotsBuy();
            case 40 -> openVisit();
        }
    }

    private void openWarp() {
        player.closeInventory();

        new PMenuWarpMenu(player).open();
    }

    private void openPlots() {
        player.closeInventory();
        new PMenuPlotsMenu(player, player.getUniqueId()).open();
    }

    private void openThisPlot() {
        player.closeInventory();
        new PMenuPlotInfoMenu(player, null).open();
    }

    private void openVisit() {
        player.closeInventory();

        new AnvilGUI.Builder()
                .itemLeft(makeItem(Material.NETHER_STAR, PMenuI18N.PLAYERLIST_PLAYER.get(player)))
                .text(PMenuI18N.PLAYERLIST_PLAYER.get(player))
                .title(PMenuI18N.PLAYERLIST_TITLE.get(player))
                .plugin(Essential.getInstance())
                .onComplete((player1, text) -> {
                    try {
                        GalaxyCoreCore core = CoreProvider.getCore();
                        Connection connection = core.getDatabaseConfiguration().getConnection();

                        // Sql Statement ignoring case
                        PreparedStatement load = connection.prepareStatement("SELECT * FROM core_playercache WHERE LOWER(`lastname`) = LOWER(?)");
                        load.setString(1, text);
                        ResultSet loadResult = load.executeQuery();

                        if (!loadResult.next()) {
                            loadResult.close();
                            load.close();
                            return AnvilGUI.Response.text(PMenuI18N.PLAYERLIST_PLAYERNOTFOUND.get(player));
                        }

                        UUID uuid = UUID.fromString(loadResult.getString("uuid"));
                        loadResult.close();

                        PMenuPlotsMenu menu = new PMenuPlotsMenu(player, uuid);
                        return AnvilGUI.Response.openInventory(menu.inv());

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .open(player);
    }

    private void openLizenz() {
        player.closeInventory();
        new PMenuLizenzMenu(player).open();
    }

    private void openPlotsBuy() {
        player.closeInventory();
        new PMenuPlotBuyMenu(player).open();
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