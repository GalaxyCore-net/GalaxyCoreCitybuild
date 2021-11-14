package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    //do one - do two

    //do 0 1 2
    //do one two
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.gamemode.self")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            if (args[0].equalsIgnoreCase("1")) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.creative"));
            } else if (args[0].equalsIgnoreCase("0")) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.survival"));
            } else if (args[0].equalsIgnoreCase("2")) {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.adventure"));
            } else if (args[0].equalsIgnoreCase("3")) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.spectator"));
            } else {
                sender.sendMessage(I18N.getByPlayer(player, "citybuild.gamemode.usage"));

            }
        }
        if (args.length == 2) {
            if (!player.hasPermission("citybuild.command.gamemode.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);

            // Mit Target meine ich den anderen Spieler der den User im Command schreibt

            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            if (args[0].equalsIgnoreCase("1")) {
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage(I18N.getByPlayer(target, "citybuild.creative"));
                player.sendMessage(I18N.getByPlayer(target, "citybuild.creative.set"));
            } else if (args[0].equalsIgnoreCase("0")) {
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage(I18N.getByPlayer(target, "citybuild.survival"));
                player.sendMessage(I18N.getByPlayer(target, "citybuild.survival.set"));
            } else if (args[0].equalsIgnoreCase("2")) {
                target.setGameMode(GameMode.ADVENTURE);
                target.sendMessage(I18N.getByPlayer(target, "citybuild.adventure"));
                player.sendMessage(I18N.getByPlayer(target, "citybuild.adventure.set"));
            } else if (args[0].equalsIgnoreCase("3")) {
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage(I18N.getByPlayer(target, "citybuild.spectator"));
                player.sendMessage(I18N.getByPlayer(target, "citybuild.spectator.set"));
            } else {
                sender.sendMessage(I18N.getByPlayer(player, "citybuild.gamemode.usage"));
            }
        }
        return true;
    }
}