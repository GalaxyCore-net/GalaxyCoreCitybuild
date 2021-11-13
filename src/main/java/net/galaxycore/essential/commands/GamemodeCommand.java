package net.galaxycore.essential.commands;

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

        if (!sender.hasPermission("Citybuild.command.gamemode")) return true;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Du musst ein Spieler sein");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("1")) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im Kreativ Modus");
            } else if (args[0].equalsIgnoreCase("0")) {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im Überlebens Modus");
            } else if (args[0].equalsIgnoreCase("2")) {
                player.setGameMode(GameMode.ADVENTURE);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im Abenteuer Modus");
            } else if (args[0].equalsIgnoreCase("3")) {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.GREEN + "Du bist nun im Spectator Modus");
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /gamemode <0|1|2|3>");

            }
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /gamemode <0|1|2|3>");
        }
        return false;
    }
}
