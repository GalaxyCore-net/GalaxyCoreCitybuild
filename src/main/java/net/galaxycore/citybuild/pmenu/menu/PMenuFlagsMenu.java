package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import lombok.Getter;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import net.galaxycore.citybuild.pmenu.menu.flags.Flag;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.Optional;

@Getter
public class PMenuFlagsMenu extends Menu {
    private final Player player;
    private Plot plot;
    private boolean open = true;

    public PMenuFlagsMenu(Player player, Plot plot) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;

        if(plot != null && !Objects.requireNonNull(plot.getOwner()).toString().equals(player.getUniqueId().toString()) && open) {
            open = false;
            PMenuI18N.NO_PLOT_PERMISSIONS.send(player);
        }
    }

    public PMenuFlagsMenu(Player player) {
        this(player, null);
        PlotAPI plotApi = new PlotAPI();

        if (plot == null) {
            Optional<PlotArea> possiblePlotRegion = plotApi.getPlotAreas(player.getWorld().getName()).stream().filter(plotArea -> plotArea.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY())).findFirst();

            if (possiblePlotRegion.isEmpty()) {
                open = false;
                PMenuI18N.NOT_ON_A_PLOT.send(player);
                return;
            }

            PlotArea plotArea = possiblePlotRegion.get();

            plot = plotArea.getPlot(Location.at(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
            if (plot == null) {
                open = false;
                PMenuI18N.NOT_ON_A_PLOT.send(player);
            }
        }

        if (plot != null && plot.getOwner() == null) {
            open = false;
            PMenuI18N.PLOT_NOT_CLAIMED.send(player);
        }

        if(plot != null && !plot.getOwner().toString().equals(player.getUniqueId().toString()) && open) {
            open = false;
            PMenuI18N.NO_PLOT_PERMISSIONS.send(player);
        }
    }

    @Override
    public void open() {
        if(open)
            super.open();
    }

    @Override
    public String getMenuName() {
        return PMenuI18N.FLAGS_TITLE.get(player);
    }

    @Override
    public int getSlots() {
        return 9*5;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        if(inventoryClickEvent.getRawSlot() == 27+9) {
            new PMenuPlotInfoMenu(player, plot).open();
            return;
        }

        if(inventoryClickEvent.getRawSlot() > 26) return;
        Flag flag = Flag.values()[inventoryClickEvent.getRawSlot()];
        flag.openUI(player, plot);
    }

    @Override
    public void setMenuItems() {
        for (Flag value : Flag.values()) {
            inventory.addItem(value.item(plot, player));
        }

        inventory.setItem(27+9, makeItem(Material.BARRIER, PMenuI18N.BACK.get(player)));
        setFillerGlass();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
