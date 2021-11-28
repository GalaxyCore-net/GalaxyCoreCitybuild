package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

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
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            if (args[0].equalsIgnoreCase("1")) {
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.creative"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.creative.set"), new LuckPermsApiWrapper(target)));
            } else if (args[0].equalsIgnoreCase("0")) {
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.survival"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.creative.set"), new LuckPermsApiWrapper(target)));
            } else if (args[0].equalsIgnoreCase("2")) {
                target.setGameMode(GameMode.ADVENTURE);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.adventure"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.creative.set"), new LuckPermsApiWrapper(target)));
            } else if (args[0].equalsIgnoreCase("3")) {
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.spectator"), new LuckPermsApiWrapper(player)));
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.creative.set"), new LuckPermsApiWrapper(target)));
            } else {
                sender.sendMessage(I18N.getByPlayer(player, "citybuild.gamemode.usage"));
            }
        }
        return true;
    }
}