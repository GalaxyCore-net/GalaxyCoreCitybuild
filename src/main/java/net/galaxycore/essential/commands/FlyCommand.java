package net.galaxycore.essential.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage("§2Du wurdest in den FlugModus gesetzt");

            } else {

                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage("§2Du wurdest aus dem FlugModus gesetzt");

            }
        }
        return false;
    }
}
