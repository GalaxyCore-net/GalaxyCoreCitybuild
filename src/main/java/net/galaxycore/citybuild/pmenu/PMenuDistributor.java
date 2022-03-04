package net.galaxycore.citybuild.pmenu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.menu.*;
import net.galaxycore.citybuild.pmenu.menu.flags.Flag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PMenuDistributor {
    public void distribute(Player player, String alias, String[] args) {
        if (alias.equalsIgnoreCase("/warp")) {
            new PMenuWarpMenu(player).open();
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
                new PMenuPlotInfoMenu(player, null).open();
            }
            if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l") || args[0].equalsIgnoreCase("find") || args[0].equalsIgnoreCase("search")) {
                new PMenuPlotsMenu(player, player.getUniqueId()).open();
            }
        }

        if(args.length >= 1) {
            if (args[0].equalsIgnoreCase("flag") || args[0].equalsIgnoreCase("f")) {
                new PMenuFlagsMenu(player).open();
            }

            if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("h")) {
                UUID toOpen = player.getUniqueId();
                if(args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if(plotPlayer == null) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    toOpen = plotPlayer.getUUID();
                }
                new PMenuPlotsMenu(player, toOpen).open();
            }
        }

        if ( args.length == 0 ) {
            new PMenuBaseMenu(player).open();
        }
    }

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new PCommandListener(), Essential.getInstance());

        PMenuI18N.TITLE_MAIN.__call__();
        //noinspection ResultOfMethodCallIgnored Registration Statement
        Flag.ANIMAL_ATTACK.getDescEn();
    }
}
