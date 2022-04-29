package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.backup.BackupManager;
import com.plotsquared.core.configuration.Settings;
import com.plotsquared.core.events.PlotFlagRemoveEvent;
import com.plotsquared.core.events.TeleportCause;
import com.plotsquared.core.location.Direction;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.PlotFlag;
import com.plotsquared.core.plot.flag.implementations.DoneFlag;
import com.plotsquared.core.util.Permissions;
import com.plotsquared.core.util.task.TaskManager;
import lombok.Getter;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.function.Function;

@Getter
public class PMenuPlotInfoConfigMenu extends Menu {
    private final Player player;
    private final Plot plot;
    private boolean open = true;

    public PMenuPlotInfoConfigMenu(Player player, Plot plot) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;

        if(plot != null && !Objects.requireNonNull(plot.getOwner()).toString().equals(player.getUniqueId().toString()) && open) {
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
        return 3*9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        player.closeInventory();
        switch (inventoryClickEvent.getRawSlot()) {
            case 18 -> new PMenuPlotInfoMenu(player, plot).open();
            case 10 -> runClear(false);
            case 12 -> runMerge();
            case 14 -> runClear(true);
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(10, makeItem(Material.FEATHER, PMenuI18N.CLEARPLOT.get(player)));
        inventory.setItem(12, makeItem(Material.ANVIL, PMenuI18N.MERGE_PLOTS.get(player)));
        inventory.setItem(14, makeItem(Material.TNT, PMenuI18N.DELETE_PLOT.get(player)));
        inventory.setItem(18, makeItem(Material.BARRIER, PMenuI18N.BACK.get(player)));
        setFillerGlass();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void runClear(boolean delete){
        new PMenuConfirmationMenu(player, (state) -> {
            if(state) {
                if (Settings.Teleport.ON_CLEAR) {
                    plot.getPlayersInPlot().forEach(playerInPlot -> plot.teleportPlayer(playerInPlot, TeleportCause.COMMAND_CLEAR,
                            result -> {
                            }
                    ));
                }
                PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());

                BackupManager.backup(plotPlayer, plot, () -> {
                    boolean result = plot.getPlotModificationManager().clear(true, delete, plotPlayer, () -> {
                        plot.getPlotModificationManager().unlink();
                        TaskManager.runTask(() -> {
                            plot.removeRunning();
                            // If the state changes, then mark it as no longer done
                            PMenuI18N.DONE.send(player);
                        });
                    });
                    if (result) {
                        plot.addRunning();
                    }
                });
            }
        }).open();
    }

    public void runMerge(){
        new PMenuConfirmationMenu(player, (state) -> {
            if(state) {
                PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());
                final int size = plot.getConnectedPlots().size();
                Direction direction = Direction.ALL;
                assert plotPlayer != null;
                int max = Permissions.hasPermissionRange(plotPlayer, "plots.merge", Settings.Limit.MAX_PLOTS);
                if (plot.getPlotModificationManager().autoMerge(Direction.ALL, max, plotPlayer.getUUID(), plotPlayer, true)) {
                    PMenuI18N.DONE.send(player);
                    return;
                }
                PMenuI18N.NOTHING_TO_MERGE.send(player);
            }
        }).open();
    }
}
