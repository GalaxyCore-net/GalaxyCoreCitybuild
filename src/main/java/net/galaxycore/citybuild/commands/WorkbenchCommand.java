package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorkbenchCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.workbench")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }
        player.openWorkbench(null, true);
        return true;
    }
}
