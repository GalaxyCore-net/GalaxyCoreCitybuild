package net.galaxycore.citybuild.pmenu.menu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

@Getter
public class PMenuFlagsMenu extends Menu {
    private final Player player;
    private final FlagMenuData data;
    private Plot plot;
    private boolean open = true;

    public PMenuFlagsMenu(Player player, Plot plot, FlagMenuData data) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
        this.player = player;
        this.plot = plot;
        this.data = data;
    }

    public PMenuFlagsMenu(Player player, Plot plot) {
        this(player, plot, new FlagMenuData());
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
            System.out.println(plot.getOwner());
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
        return 9*6;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {

    }

    @Override
    public void setMenuItems() {

        inventory.setItem(45, makeItem(Material.BARRIER, PMenuI18N.BACK.get(player)));
        inventory.setItem(48, makeItem(Material.OAK_BUTTON, PMenuI18N.PREV.get(player)));
        inventory.setItem(49, makeItem(Material.PAPER, PMenuI18N.PAGE.get(player) + (data.page+1)));
        inventory.setItem(50, makeItem(Material.OAK_BUTTON, PMenuI18N.NEXT.get(player)));
        inventory.setItem(53, makeItem(Material.SPRUCE_SIGN, PMenuI18N.SEARCH.get(player)));

        setFillerGlass();
    }

    @Data
    public static class FlagMenuData {
        private int page = 0;
        private String query = "";
    }
}
