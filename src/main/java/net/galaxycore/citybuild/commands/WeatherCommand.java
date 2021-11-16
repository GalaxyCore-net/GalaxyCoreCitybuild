package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WeatherCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.weather")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }
        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("clear")) {
                player.getLocation().getWorld().setStorm(false);
                player.getLocation().getWorld().setThundering(false);
                player.getLocation().getWorld().setClearWeatherDuration(100000);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.weather.clear"));
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer != player)
                        onlinePlayer.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.weather.global.clear"), new LuckPermsApiWrapper(player)));
                }
            } else if (args[0].equalsIgnoreCase("rain")) {
                player.getLocation().getWorld().setStorm(true);
                player.getLocation().getWorld().setThundering(false);
                player.getLocation().getWorld().setWeatherDuration(100000);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.weather.rain"));
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer != player)
                        onlinePlayer.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.weather.global.rain"), new LuckPermsApiWrapper(player)));
                }
            } else if (args[0].equalsIgnoreCase("thunder")) {
                player.getLocation().getWorld().setThundering(true);
                player.getLocation().getWorld().setStorm(true);
                player.getLocation().getWorld().setThunderDuration(100000);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.weather.thunder"));
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer != player)
                        onlinePlayer.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.weather.global.thunder"), new LuckPermsApiWrapper(player)));
                }
                // TODO Cooldown mit Bypass Permissions
            }
        } else if (args.length == 0) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.weather.clear"));
            player.getLocation().getWorld().setStorm(false);
            player.getLocation().getWorld().setThundering(false);
            player.getLocation().getWorld().setClearWeatherDuration(100000);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer != player)
                    onlinePlayer.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.weather.global.clear"), new LuckPermsApiWrapper(player)));
            }


        }
        return true;
    }
}
