package net.galaxycore.citybuild.commands;

import org.bukkit.Bukkit;
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
            if (!player.hasPermission("citybuild.command.fly.self")) {
                player.sendMessage("§2Du hast keine Berechtigung diesen Command zu benutzen!");
                return true;
            }
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage("§2Du wurdest in den FlugModus gesetzt");

            } else {

                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage("§2Du wurdest aus dem FlugModus gesetzt");

            }
        } else if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.fly.other")) {
                player.sendMessage("§2Du hast keine Berechtigung diesen Command zu benutzen!");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("Dieser Spieler ist nicht online");
                return true;
            }
            if (!target.getAllowFlight()) {
                target.setAllowFlight(true);
                target.setFlying(true);
                target.sendMessage("§2Du wurdest von " + sender.getName() + "§2in den Flugmodus gesetzt");
                player.sendMessage("§2Du hast" + target.getName() + "§2in den FlugModus gesetzt");


            } else {

                target.setAllowFlight(false);
                target.setFlying(false);
                target.sendMessage("§2Du wurdest von " + sender.getName() + "§2aus dem Flugmodus gesetzt");
                player.sendMessage("§2Du hast" + player.getName() + "§2aus dem FlugModus gesetzt");

            }
        }
        return false;
    }
}
