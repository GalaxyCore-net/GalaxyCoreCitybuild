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

public class FeedCommand implements CommandExecutor {
    private final HashMap<Player, Long> feedCooldown = new HashMap<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("citybuild.command.feed.self")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            if (feedCooldown.containsKey(player)) {
                if (!player.hasPermission("citybuild.command.day.cooldown.bypass")) {
                    if (feedCooldown.get(player) < System.currentTimeMillis()) {
                        feedCooldown.remove(player);
                    } else {
                        player.sendMessage(I18N.getByPlayer(player, "citybuild.cooldown")
                                .replace("%time%", new SimpleDateFormat(
                                        I18N.getInstanceRef().get().getLanguages()
                                                .get(I18N.getInstanceRef().get().getLocale(player)).getDateFormat() + " " +
                                                I18N.getInstanceRef().get().getLanguages()
                                                        .get(I18N.getInstanceRef().get().getLocale(player)).getTimeFormat())
                                        .format(new Date(feedCooldown.get(player)))));
                    }
                }
            }
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.sendMessage(I18N.getByPlayer(player, "citybuild.feed"));
            feedCooldown.put(player, System.currentTimeMillis() + 300000); // 300000ms = 5m
        } else if (args.length == 1) {

            if (!player.hasPermission("citybuild.command.feed.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            target.setFoodLevel(20);
            target.setSaturation(20);
            target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.feed.other"), new LuckPermsApiWrapper(player)));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.feed.other.notify"), new LuckPermsApiWrapper(target)));
        }
        return true;
    }
}

