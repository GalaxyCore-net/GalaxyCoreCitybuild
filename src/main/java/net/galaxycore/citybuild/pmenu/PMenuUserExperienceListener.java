package net.galaxycore.citybuild.pmenu;


import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.events.PlayerEnterPlotEvent;
import com.plotsquared.core.plot.flag.implementations.PriceFlag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class PMenuUserExperienceListener {
    public PMenuUserExperienceListener() {
        new PlotAPI().registerListener(this);
    }

    @Subscribe
    public void onEnterPlot(PlayerEnterPlotEvent event) {
        if (event.getPlot().getFlagContainer().getFlag(PriceFlag.class) != PriceFlag.PRICE_NOT_BUYABLE) {
            Player player =  Objects.requireNonNull(Bukkit.getPlayer(event.getPlotPlayer().getUUID()));
            player.sendMessage(PMenuI18N.YOU_CAN_BUY_THIS_PLOT_FOR_COINS.get(player).replace("%price%", event.getPlot().getFlagContainer().getFlag(PriceFlag.class).getValue() + ""));
        }
    }
}
