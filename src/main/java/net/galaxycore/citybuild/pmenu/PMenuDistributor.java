package net.galaxycore.citybuild.pmenu;

import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.menu.PMenuBaseMenu;
import net.galaxycore.citybuild.pmenu.menu.PMenuPlotInfoMenu;
import net.galaxycore.citybuild.pmenu.menu.PMenuWarpMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        }

        if ( args.length == 0 ) {
            new PMenuBaseMenu(player).open();
        }
    }

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new PCommandListener(), Essential.getInstance());

        PMenuI18N.TITLE_MAIN.__call__();
    }
}
