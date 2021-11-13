package net.galaxycore.essential.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0){
            player.setHealth(20);
            player.sendMessage("§2Du wurdest geheilt");
            player.setFoodLevel(20);
            player.setFireTicks(0);

        }
        return false;


    }
}