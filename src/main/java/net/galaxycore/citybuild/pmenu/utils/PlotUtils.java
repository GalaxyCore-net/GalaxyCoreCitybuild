package net.galaxycore.citybuild.pmenu.utils;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import net.galaxycore.citybuild.pmenu.PMenuI18N;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Optional;

public class PlotUtils {

    @Nullable
    public static Plot getPlotForPlayer(Player player) {
        return getPlotForPlayer(player, true);
    }

    @Nullable
    public static Plot getPlotForPlayer(Player player, boolean checkPerms) {
        PlotAPI plotApi = new PlotAPI();
        @Nullable Plot plot;

        Optional<PlotArea> possiblePlotRegion = plotApi.getPlotAreas(player.getWorld().getName()).stream().filter(plotArea -> plotArea.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY())).findFirst();

        if (possiblePlotRegion.isEmpty()) {
            PMenuI18N.NOT_ON_A_PLOT.send(player);
            return null;
        }

        PlotArea plotArea = possiblePlotRegion.get();

        plot = plotArea.getPlot(Location.at(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
        if (plot == null) {
            PMenuI18N.NOT_ON_A_PLOT.send(player);
            return null;
        }

        if (plot.getOwner() == null) {
            PMenuI18N.PLOT_NOT_CLAIMED.send(player);
            return null;
        }

        if (!plot.getOwner().toString().equals(player.getUniqueId().toString()) && checkPerms) {
            PMenuI18N.NO_PLOT_PERMISSIONS.send(player);
            return null;
        }

        return plot;
    }

    public static org.bukkit.Location p2LocationToBukkitLocation(Location source) {
        return new org.bukkit.Location((World) source.getWorld().getPlatformWorld(), source.getX(), source.getY(), source.getZ());
    }
}