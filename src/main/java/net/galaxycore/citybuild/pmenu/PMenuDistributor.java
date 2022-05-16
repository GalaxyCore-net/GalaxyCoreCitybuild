package net.galaxycore.citybuild.pmenu;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.database.DBFunc;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.menu.*;
import net.galaxycore.citybuild.pmenu.menu.flags.Flag;
import net.galaxycore.citybuild.pmenu.utils.PlotUtils;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class PMenuDistributor {
    public void distribute(Player player, String alias, String[] args) {
        if (alias.equalsIgnoreCase("/warp")) {
            new PMenuWarpMenu(player).open();
            return;
        }
        if (alias.equalsIgnoreCase("/help")) {
            player.sendMessage(I18N.getS(player, "citybuild.helptext"));
            return;
        } else if (alias.equalsIgnoreCase("/licence") || alias.equalsIgnoreCase("/lizenz")) {
            new PMenuLizenzMenu(player).open();
            return;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
                new PMenuPlotInfoMenu(player, null).open();
                return;
            } else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l") || args[0].equalsIgnoreCase("find") || args[0].equalsIgnoreCase("search")) {
                new PMenuPlotsMenu(player, player.getUniqueId()).open();
                return;
            } else if (args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("c")) {
                new PMenuClaimMenu(player).open();
                return;
            } else if (args[0].equalsIgnoreCase("auto") || args[0].equalsIgnoreCase("a")) {
                new PMenuAutoMenu(player).open();
                return;
            } else if (List.of("sethome", "sh", "seth").contains(args[0])) {
                new PMenuSetHomeMenu(player).open();
                return;
            } else if (args[0].equalsIgnoreCase("clear")) {
                @Nullable Plot plot = PlotUtils.getPlotForPlayer(player);
                if (plot != null) {
                    new PMenuPlotInfoConfigMenu(player, plot).runClear(false);
                }
                return;
            } else if (args[0].equalsIgnoreCase("merge")) {
                @Nullable Plot plot = PlotUtils.getPlotForPlayer(player);
                if (plot != null) {
                    new PMenuPlotInfoConfigMenu(player, plot).runMerge();
                }
                return;
            } else if (args[0].equalsIgnoreCase("buy")) {
                @Nullable Plot plot = PlotUtils.getPlotForPlayer(player, false);
                if (plot != null) {
                    new PMenuBuyPlotMenu(player).open();
                }
                return;
            } else if (args[0].equalsIgnoreCase("delete")) {
                @Nullable Plot plot = PlotUtils.getPlotForPlayer(player);
                if (plot != null) {
                    new PMenuPlotInfoConfigMenu(player, plot).runClear(true);
                }
                return;
            } else if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage(I18N.getS(player, "citybuild.helptext"));
                return;
            } else if (args[0].equalsIgnoreCase("middle")) {
                @Nullable Plot plot = PlotUtils.getPlotForPlayer(player);
                if (plot != null) {
                    plot.getCenter(location -> {
                        player.teleport(PlotUtils.p2LocationToBukkitLocation(location));
                        PMenuI18N.TELEPORTED.send(player);
                    });
                }
                return;
            }
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("flag") || args[0].equalsIgnoreCase("f")) {
                new PMenuFlagsMenu(player).open();
                return;
            }

            if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("h")) {
                UUID toOpen = player.getUniqueId();
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    new PMenuPlotsMenu(player, plotPlayer.getUUID()).open();
                }
                new PMenuPlotsMenu(player, toOpen).open();

                return;
            }
            if (List.of("setowner", "owner", "so", "seto").contains(args[0])) {
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    new PMenuSetOwnerMenu(player, plotPlayer.getUUID()).open();

                } else {
                    new PMenuSearchPlayerMenu(player, (offlinePlayer) -> new PMenuSetOwnerMenu(player, offlinePlayer.getUniqueId()).open()).open();
                }
                return;
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    new PMenuAddMenu(player, plotPlayer).open();
                } else {
                    new PMenuSearchPlayerMenu(player, (offlinePlayer) -> {
                        PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(offlinePlayer.getUniqueId());
                        new PMenuAddMenu(player, plotPlayer).open();
                    }).open();
                }
                return;
            }

            if (List.of("trust", "t").contains(args[0])) {
                if (args.length > 1) {
                    PlotPlayer<?> plotPLayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPLayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    new PMenuTrustMenu(player, plotPLayer).open();
                } else {
                    new PMenuSearchPlayerMenu(player, (offlinePlayer) -> {
                        PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(offlinePlayer.getUniqueId());
                        new PMenuTrustMenu(player, plotPlayer).open();
                    }).open();
                }

                return;
            }

            if (List.of("remove", "r", "untrust", "ut", "undeny", "ud", "unban").contains(args[0])) {
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    new PMenuRemoveMenu(player, plotPlayer).open();
                } else {
                    new PMenuSearchPlayerMenu(player, (offlinePlayer) -> {
                        PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(offlinePlayer.getUniqueId());
                        new PMenuRemoveMenu(player, plotPlayer).open();
                    }).open();
                }

                return;
            }

            if (List.of("deny", "d", "ban").contains(args[0])) {
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    new PMenuDenyMenu(player, plotPlayer).open();
                } else {
                    new PMenuSearchPlayerMenu(player, (offlinePlayer) -> {
                        PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(offlinePlayer.getUniqueId());
                        new PMenuDenyMenu(player, plotPlayer).open();
                    }).open();
                }

                return;
            }

            if (List.of("v", "visit").contains(args[0])) {
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null || args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }
                    new PMenuPlotsMenu(player, plotPlayer.getUUID()).open();
                } else new PMenuSearchPlayerMenu(player, (offlinePlayer) -> {
                    new PMenuPlotsMenu(player, offlinePlayer.getUniqueId()).open();
                }).open();
                return;
            }

            if (List.of("kick", "k").contains(args[0])) {
                if (args.length > 1) {
                    PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(args[1]);

                    if (plotPlayer == null && !args[1].equalsIgnoreCase("*")) {
                        PMenuI18N.PLAYERNOTFOUND.send(player);
                        return;
                    }

                    UUID toKick = plotPlayer != null ? plotPlayer.getUUID() : DBFunc.EVERYONE;
                    new PMenuKickMenu(player, toKick).open();
                } else {
                    new PMenuSearchPlayerMenu(player, (offlinePlayer) -> new PMenuKickMenu(player, offlinePlayer.getUniqueId()).open()).open();
                }

                return;
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
                return;
            }

            if (List.of("setbiome", "biome", "sb", "setb", "b").contains(args[0])) {
                if (args.length > 1) {
                    new PMenuSetBiomeMenu(player, args[1]).open();
                } else {
                    new PMenuSetBiomeMenu(player, "null").open();
                }
                return;
            }

        } else {
            new PMenuBaseMenu(player).open();
            return;
        }
        PMenuI18N.PMENU_HELP.send(player);
    }

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new PCommandListener(), Essential.getInstance());

        PMenuI18N.TITLE_MAIN.__call__();
        //noinspection ResultOfMethodCallIgnored Registration Statement
        Flag.ANIMAL_ATTACK.getDescEn();
    }
}
