package net.galaxycore.citybuild.pmenu;

import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.menu.PMenuBaseMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PMenuDistributor {
    public void distribute(Player player, String[] args) {
        if ( args.length == 0 ) {
            new PMenuBaseMenu(player).open();
        }
    }

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new PCommandListener(), Essential.getInstance());

        PMenuI18N.TITLE_MAIN.__call__();
    }
}
