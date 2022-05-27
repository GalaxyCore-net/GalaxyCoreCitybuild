package net.galaxycore.citybuild.utils;

import com.plotsquared.core.configuration.ConfigurationUtil;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.sk89q.worldedit.function.pattern.Pattern;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BorderChanger {

    public static void change(Type componentType, Player player, Material materialToSet) {

        PlotPlayer<Player> plotPlayer = PlotPlayer.from(player);
        Plot plot = plotPlayer.getCurrentPlot();
        String type = componentType.getType();
        Pattern pattern = ConfigurationUtil.BLOCK_BUCKET.parseString(materialToSet.toString()).toPattern();
        if (plot.getConnectedPlots().size() > 1) {

            for (Plot plots : plot.getConnectedPlots()) {
                plots.getPlotModificationManager().setComponent(type, pattern, null, null);
            }
        } else {
            plot.getPlotModificationManager().setComponent(type, pattern, null, null);
        }
    }

    public enum Type {
        WALL("wall"),
        BORDER("border");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}