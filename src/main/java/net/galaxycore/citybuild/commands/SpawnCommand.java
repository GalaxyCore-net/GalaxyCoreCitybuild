package net.galaxycore.citybuild.commands;

import net.galaxycore.citybuild.listeners.PlayerJoinListener;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("citybuild.command.spawn.self")) { // Alle
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            player.teleport(PlayerJoinListener.getSpawnLoc());
            player.sendMessage(I18N.getByPlayer(player, "citybuild.spawn.self"));
        } else if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.spawn.other")) { // SrSupporter + höher
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;

            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            target.teleport(PlayerJoinListener.getSpawnLoc());
            target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.spawn.other"), new LuckPermsApiWrapper(player)));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.spawn.other.notify"), new LuckPermsApiWrapper(target)));
        }else {
            player.sendMessage((I18N.getByPlayer(player, "citybuild.spawn.usage")));
        }


        return true;
    }
}
