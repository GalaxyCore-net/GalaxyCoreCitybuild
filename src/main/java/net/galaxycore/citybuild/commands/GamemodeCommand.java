package net.galaxycore.citybuild.commands;

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

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Du musst ein Spieler sein");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.gamemode.self")) {
                player.sendMessage("§2Du hast keine Berechtigung diesen Command zu benutzen!");
                return true;
            }
            if (args[0].equalsIgnoreCase("1")) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im KreativModus");
            } else if (args[0].equalsIgnoreCase("0")) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im ÜberlebensModus");
            } else if (args[0].equalsIgnoreCase("2")) {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im AbenteuerModus");
            } else if (args[0].equalsIgnoreCase("3")) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im SpectatorModus");
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /gamemode <0|1|2|3>");

            }
        }
        if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.gamemode.other")) {
                player.sendMessage("§2Du hast keine Berechtigung diesen Command zu benutzen!");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("Dieser Spieler ist nicht online");
                return true;
            }
            if (args[0].equalsIgnoreCase("1")) {
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage(ChatColor.GREEN + "Du bist nun im KreativModus");
                player.sendMessage(ChatColor.GREEN + "Du bist hast im KrjjativModus");
            } else if (args[0].equalsIgnoreCase("0")) {
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage(ChatColor.GREEN + "Du bist nun im ÜberlebensModus");
                player.sendMessage(ChatColor.GREEN + "Du bist nun im KreativModus");
            } else if (args[0].equalsIgnoreCase("2")) {
                target.setGameMode(GameMode.ADVENTURE);
                target.sendMessage(ChatColor.GREEN + "Du bist nun im AbenteuerModus");
                player.sendMessage(ChatColor.GREEN + "Du bist nun im KreativModus");
            } else if (args[0].equalsIgnoreCase("3")) {
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage(ChatColor.GREEN + "Du bist nun im SpectatorModus");
                player.sendMessage(ChatColor.GREEN + "Du bist nun im KreativModus");
            } else {

            }
            sender.sendMessage(ChatColor.RED + "Usage: /gamemode <0|1|2|3>");
        }
        return false;
    }
}
