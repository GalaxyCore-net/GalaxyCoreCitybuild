package net.galaxycore.citybuild.pmenu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.database.DBFunc;
import com.plotsquared.core.player.PlotPlayer;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.menu.*;
import net.galaxycore.citybuild.pmenu.menu.flags.Flag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
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
            if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l") || args[0].equalsIgnoreCase("find") || args[0].equalsIgnoreCase("search")) {
                new PMenuPlotsMenu(player, player.getUniqueId()).open();
            }
            if (args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("c")) {
                new PMenuClaimMenu(player).open();
            }
            if (args[0].equalsIgnoreCase("auto") || args[0].equalsIgnoreCase("a")) {
                new PMenuAutoMenu(player).open();
            }
            if (List.of("sethome", "sh", "seth").contains(args[0])) {
                new PMenuSetHomeMenu(player).open();
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

                    if (plotPlayer == null) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    toOpen = plotPlayer.getUUID();
                }
                new PMenuPlotsMenu(player, toOpen).open();
            }
            if (List.of("setowner", "owner", "so", "seto").contains(args[0])) {
                UUID newOwner = player.getUniqueId();
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    newOwner = plotPlayer.getUUID();

                }
                new PMenuSetOwnerMenu(player, newOwner).open();
            }

            if (args[0].equalsIgnoreCase("add")) {
                PlotPlayer<?> toAdd = new PlotAPI().wrapPlayer(player.getUniqueId());
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    toAdd = plotPlayer;
                }

                new PMenuAddMenu(player, toAdd).open();
            }

            if (List.of("trust", "t").contains(args[0])) {
                PlotPlayer<?> toTrust = new PlotAPI().wrapPlayer(player.getUniqueId());
                if (args.length > 1) {
                    PlotPlayer<?> plotPLayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPLayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    toTrust = plotPLayer;
                }

                new PMenuTrustMenu(player, toTrust).open();
            }

            if (List.of("remove", "r", "untrust", "ut", "undeny", "ud", "unban").contains(args[0])) {
                PlotPlayer<?> toRemove = new PlotAPI().wrapPlayer(player.getUniqueId());
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    toRemove = plotPlayer;
                }

                new PMenuRemoveMenu(player, toRemove).open();
            }

            if (List.of("deny", "d", "ban").contains(args[0])) {
                PlotPlayer<?> toDeny = new PlotAPI().wrapPlayer(player.getUniqueId());
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    toDeny = plotPlayer;
                }

                new PMenuDenyMenu(player, toDeny).open();
            }

            if (List.of("kick", "k").contains(args[0])) {
                UUID toKick = player.getUniqueId();
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    toKick = plotPlayer != null ? plotPlayer.getUUID() : DBFunc.EVERYONE;
                }

                new PMenuKickMenu(player, toKick).open();
            }

            if (List.of("alias", "setalias", "sa", "name", "rename", "setname", "seta", "nameplot").contains(args[0])) {
                if (args.length > 1) {
                    String newAlias = null;
                    if (args.length > 2) {
                        newAlias = args[2];
                    }
                    new PMenuAliasMenu(player, args[1], newAlias).open();
                } else {
                    new PMenuAliasMenu(player, null, null).open();
                }
            }

            if (List.of("setdescription", "desc", "setdesc", "setd", "description").contains(args[0])) {
                if (args.length > 1) {
                    new PMenuSetDescriptionMenu(player, args[1]).open();
                } else {
                    new PMenuSetDescriptionMenu(player, null).open();
                }
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
