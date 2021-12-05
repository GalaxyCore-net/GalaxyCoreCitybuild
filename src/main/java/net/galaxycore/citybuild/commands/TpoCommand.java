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

import java.util.Objects;

public class TpoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.tpo")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            player.teleport(Objects.requireNonNull(target));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.tpo.self"), new LuckPermsApiWrapper(target)));
        } else if (args.length == 2) {
            if (!player.hasPermission("citybuild.command.tpo.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player targetToTeleportTo = Bukkit.getPlayer(args[1]);
            if (targetToTeleportTo == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            Player targetToTeleport = Bukkit.getPlayer(args[0]);
            if (targetToTeleport == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            targetToTeleport.teleport(targetToTeleportTo);
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.tpo.target.tpo.notify"), new LuckPermsApiWrapper(player)));
        }else {
            player.sendMessage((I18N.getByPlayer(player, "citybuild.tpo.usage")));
        }
        return true;
    }
}