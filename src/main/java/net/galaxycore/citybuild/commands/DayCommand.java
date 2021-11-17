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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DayCommand implements CommandExecutor {
    private HashMap<Player, Long> dayCooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.day")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }
        if (dayCooldown.containsKey(player)) {
            if (!player.hasPermission("citybuild.command.day.cooldown.bypass")) {
                if (dayCooldown.get(player) < System.currentTimeMillis()) {
                    dayCooldown.remove(player);
                } else {
                    player.sendMessage(I18N.getByPlayer(player, "citybuild.cooldown")
                            .replace("%time%", new SimpleDateFormat(
                                    I18N.getInstanceRef().get().getLanguages()
                                            .get(I18N.getInstanceRef().get().getLocale(player)).getDateFormat() + " " +
                                            I18N.getInstanceRef().get().getLanguages()
                                                    .get(I18N.getInstanceRef().get().getLocale(player)).getTimeFormat())
                                    .format(new Date(dayCooldown.get(player)))));
                    return true;
                }
            }
        }
        player.getLocation().getWorld().setTime(6000);
        player.sendMessage(I18N.getByPlayer(player, "citybuild.time.day"));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer != player)
                if (player.getLocation().getWorld() == onlinePlayer.getLocation().getWorld())
                    onlinePlayer.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.day.global"), new LuckPermsApiWrapper(player)));
        }
        dayCooldown.put(player, System.currentTimeMillis() + 600000); // 600000ms = 10m
        return true;
    }
}
