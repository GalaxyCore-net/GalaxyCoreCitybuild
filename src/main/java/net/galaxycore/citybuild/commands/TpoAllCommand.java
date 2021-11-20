package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpoAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.tpoall")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }
        for (Player target :
                Bukkit.getOnlinePlayers()) {
            if (!target.hasPermission("citybuild.command.tpoall.immune"))
                target.teleport(player);
        }
        player.sendMessage(I18N.getByPlayer(player, "citybuild.tpoall"));
        return true;
    }
}