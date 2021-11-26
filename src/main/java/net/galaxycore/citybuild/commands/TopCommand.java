package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("citybuild.command.top")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            player.teleport(new Location(player.getLocation().getWorld(), player.getLocation().getWorld().getHighestBlockAt(player.getLocation()).getLocation().getX(), player.getLocation().getWorld().getHighestBlockAt(player.getLocation()).getLocation().getY() + 1, player.getLocation().getWorld().getHighestBlockAt(player.getLocation()).getLocation().getZ()));
            player.sendMessage(I18N.getByPlayer(player, "citybuild.top.self"));
        } else if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.top.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            target.teleport(new Location(target.getLocation().getWorld(), target.getLocation().getWorld().getHighestBlockAt(target.getLocation()).getLocation().getX(), target.getLocation().getWorld().getHighestBlockAt(target.getLocation()).getLocation().getY() + 1, target.getLocation().getWorld().getHighestBlockAt(target.getLocation()).getLocation().getZ()));
            target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.top.other"), new LuckPermsApiWrapper(player)));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.top.other.notify"), new LuckPermsApiWrapper(target)));
        } else {
            player.sendMessage((I18N.getByPlayer(player, "citybuild.top.usage")));
        }
        return true;
    }
}