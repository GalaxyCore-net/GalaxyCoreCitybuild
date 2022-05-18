package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.configuration.Settings;
import com.plotsquared.core.events.TeleportCause;
import com.plotsquared.core.location.Direction;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.Permissions;
import com.plotsquared.core.util.task.TaskManager;
import lombok.Getter;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

@Getter
public class PMenuPlotInfoConfigMenu extends Menu {
    private final Player player;
    private final Plot plot;
    private boolean open = true;

    public PMenuPlotInfoConfigMenu(Player player, Plot plot) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;

        if (plot != null && !Objects.requireNonNull(plot.getOwner()).toString().equals(player.getUniqueId().toString()) && open) {
            open = false;
            PMenuI18N.NO_PLOT_PERMISSIONS.send(player);
        }
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.FLAGS_TITLE.get(player);
    }

    @Override
    public int getSlots() {
        return 3 * 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        player.closeInventory();
        switch (inventoryClickEvent.getRawSlot()) {
            case 18 -> new PMenuPlotInfoMenu(player, plot).open();
            case 10 -> runClear(false);
            case 13 -> runMerge();
            case 16 -> runClear(true);
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(10, makeItem(Material.FEATHER, PMenuI18N.CLEARPLOT.get(player)));
        inventory.setItem(13, makeItem(Material.ANVIL, PMenuI18N.MERGE_PLOTS.get(player)));
        inventory.setItem(16, makeItem(Material.TNT, PMenuI18N.DELETE_PLOT.get(player)));
        inventory.setItem(18, makeItem(Material.BARRIER, PMenuI18N.BACK.get(player)));
        setFillerGlass();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void runClear(boolean delete) {
        new PMenuConfirmationMenu(player, (state) -> {
            if (state) {
                plot.getPlayersInPlot().forEach(playerInPlot -> plot.getHome(location -> playerInPlot.teleport(location, TeleportCause.UNKNOWN)));
                plot.getPlotModificationManager().unlink();
                if (delete) {
                    // Delete plot and all blocks via PlotSquared

                    plot.getPlotModificationManager().deletePlot(null, () -> TaskManager.runTask(() -> {
                        plot.removeRunning();
                        // If the state changes, then mark it as no longer done
                        PMenuI18N.DONE.send(player);
                    }));
                    plot.addRunning();
                    return;
                }


                plot.getPlotModificationManager().clear(() -> TaskManager.runTask(() -> {
                    plot.removeRunning();
                    // If the state changes, then mark it as no longer done
                    PMenuI18N.DONE.send(player);
                }));
                plot.addRunning();
            }
        }).open();
    }

    public void runMerge() {
        new PMenuConfirmationMenu(player, (state) -> {
            if (state) {
                PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());
                final int size = plot.getConnectedPlots().size();
                assert plotPlayer != null;
                int max = Permissions.hasPermissionRange(plotPlayer, "plots.merge", Settings.Limit.MAX_PLOTS);
                if (plot.getPlotModificationManager().autoMerge(Direction.ALL, max - size, plotPlayer.getUUID(), plotPlayer, true)) {
                    PMenuI18N.DONE.send(player);
                    return;
                }
                PMenuI18N.NOTHING_TO_MERGE.send(player);
            }
        }).open();
    }
}
