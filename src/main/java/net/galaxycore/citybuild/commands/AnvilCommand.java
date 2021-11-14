package net.galaxycore.citybuild.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AnvilCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.anvil")) {
            player.sendMessage("§2Du hast keine Berechtigung diesen Command zu benutzen!");
            return true;
        }
        player.openAnvil(null, true);

        return false;

    }
}