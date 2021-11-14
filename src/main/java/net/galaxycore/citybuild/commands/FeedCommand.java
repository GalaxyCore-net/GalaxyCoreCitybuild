package net.galaxycore.citybuild.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;

        if (args.length == 0) {


            if (!player.hasPermission("citybuild.command.feed.self")) {
                player.sendMessage("§2Du hast keine Berechtigung diesen Command zu benutzen!");
                return true;
            }
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.sendMessage("§2Du wurdest gefüttert.");
        } else if (args.length == 1) {

            if (!player.hasPermission("citybuild.command.feed.other")) {
                player.sendMessage("§2Du hast keine Berechtigung diesen Command zu benutzen!");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("Dieser Spieler ist nicht online");
                return true;
            }
            target.setFoodLevel(20);
            target.setSaturation(20);
            target.sendMessage("§2Du wurdest von " + sender.getName() + "§2 gefüttert");
            player.sendMessage("§2Du hast" + target.getName() + "§2 gefüttert");


        }
        return false;
    }
}
