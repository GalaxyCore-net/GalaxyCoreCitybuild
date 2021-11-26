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

import java.util.ArrayList;

public class GodCommand implements CommandExecutor {
    public static ArrayList<Player> godMode = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("citybuild.command.god.self")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            if (godMode.contains(player)) {
                godMode.remove(player);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.god.off"));
            } else {
                godMode.add(player);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setFireTicks(0);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.god.on"));
            }
        } else if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.god.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            if (godMode.contains(target)) {
                godMode.remove(target);
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.god.off.other"), new LuckPermsApiWrapper(target)));
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.god.off.other.notify"), new LuckPermsApiWrapper(player)));
            } else {
                godMode.add(target);
                target.setHealth(20);
                target.setFoodLevel(20);
                target.setFireTicks(0);
                player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.god.on.other"), new LuckPermsApiWrapper(target)));
                target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.god.on.other.notify"), new LuckPermsApiWrapper(player)));
            }
        } else {
            player.sendMessage((I18N.getByPlayer(player, "citybuild.god.usage")));
        }
        return true;
    }
}