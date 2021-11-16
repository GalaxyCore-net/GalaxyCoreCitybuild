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

public class DayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.day")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }

        player.getLocation().getWorld().setTime(6000);
        player.sendMessage(I18N.getByPlayer(player, "citybuild.time.day"));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer != player)
                onlinePlayer.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.time.global"), new LuckPermsApiWrapper(player)));
        }
//TODO Cooldown mit Bypass Permission

        return true;
    }
}
