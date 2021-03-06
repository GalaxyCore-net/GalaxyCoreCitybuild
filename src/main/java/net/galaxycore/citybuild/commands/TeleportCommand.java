package net.galaxycore.citybuild.commands;

import lombok.SneakyThrows;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class TeleportCommand implements CommandExecutor {
    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.tp")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            PreparedStatement statementIsPlayerLocked = Essential.getCore().getDatabaseConfiguration().getConnection().prepareStatement("SELECT tptoggle FROM galaxycity_playerdb where ID = ? ");
            statementIsPlayerLocked.setInt(1, PlayerLoader.load(target).getId());
            ResultSet resultIsPlayerLocked = statementIsPlayerLocked.executeQuery();
            if (resultIsPlayerLocked.next()) {
                if (resultIsPlayerLocked.getBoolean("tptoggle")) {
                    player.sendMessage((I18N.getByPlayer(player, "citybuild.tptoggle")));
                    resultIsPlayerLocked.close();
                    statementIsPlayerLocked.close();
                    return true;
                }
                resultIsPlayerLocked.close();
                statementIsPlayerLocked.close();
            }
            player.teleport(Objects.requireNonNull(target));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.tp.self"), new LuckPermsApiWrapper(target)));
            target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.tp.other"), new LuckPermsApiWrapper(player)));
        } else if (args.length == 2) {
            if (!player.hasPermission("citybuild.command.tp.other")) {
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
            targetToTeleport.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.tp.target2"), new LuckPermsApiWrapper(player)).replace("%target1%", targetToTeleport.getName()).replace("%target2%", targetToTeleportTo.getName()));
            targetToTeleportTo.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.tp.target1"), new LuckPermsApiWrapper(player)).replace("%target1%", targetToTeleport.getName()).replace("%target2%", targetToTeleportTo.getName()));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.tp.target.tp.notify"), new LuckPermsApiWrapper(player)).replace("%target1%", targetToTeleport.getName()).replace("%target2%", targetToTeleportTo.getName()));
        }else {
            player.sendMessage((I18N.getByPlayer(player, "citybuild.teleport.usage")));
        }
        return true;
    }
}