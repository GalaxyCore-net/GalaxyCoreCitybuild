package net.galaxycore.citybuild.pmenu;

import net.galaxycore.citybuild.Essential;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class PCommandListener implements Listener {
    /**
     * TODO: Buy plots from others
     */

    private final static List<String> pCommands = Arrays.asList(
            "plots",
            "p",
            "plot",
            "ps",
            "plotsquared",
            "p2",
            "2",
            "plotme",
            "plotsquared:plots",
            "plotsquared:p",
            "plotsquared:plot",
            "plotsquared:ps",
            "plotsquared:plotsquared",
            "plotsquared:p2",
            "plotsquared:2",
            "plotsquared:plotme",
            "warp",
            "lizenz",
            "licence"
    );

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();

        command = command.toLowerCase();
        command = command.split(" ")[0];
        command = command.replace("/", "");

        if(!PCommandListener.pCommands.contains(command)) return;

        event.setCancelled(true);

        String[] argsWithCommand = event.getMessage().split(" ");
        String[] args = new String[argsWithCommand.length - 1];
        String alias = "";

        boolean doCMD = false;

        int i = 0;

        for (String arg : argsWithCommand) {
            if(!doCMD) {
                alias = arg;
                doCMD = true;
                continue;
            }

            args[i] = arg;
            i++;
        }

        Runtime.getRuntime().gc();

        if(alias.equalsIgnoreCase("2") && event.getPlayer().hasPermission("citybuild.canUsePlotSquared")) {
            event.setCancelled(false);
            return;
        }
        Essential.getInstance().getPMenuDistributor().distribute(event.getPlayer(), alias, args);
    }
}
