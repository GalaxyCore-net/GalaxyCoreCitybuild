package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("citybuild.command.fly.self")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.fly.on"));

            } else {

                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.fly.off"));

            }
        } else if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.fly.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            if (!target.getAllowFlight()) {
                target.setAllowFlight(true);
                target.setFlying(true);
                target.sendMessage(I18N.getByPlayer(player, "citybuild.fly.on.other"));
                player.sendMessage(I18N.getByPlayer(player, "citybuild.fly.set.on"));


            } else {

                target.setAllowFlight(false);
                target.setFlying(false);
                target.sendMessage(I18N.getByPlayer(player, "citybuild.fly.off.other"));
                player.sendMessage(I18N.getByPlayer(player, "citybuild.fly.set.off"));

            }
        }
        return false;
    }
}
