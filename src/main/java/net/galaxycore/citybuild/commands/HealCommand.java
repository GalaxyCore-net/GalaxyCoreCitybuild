package net.galaxycore.citybuild.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class HealCommand implements CommandExecutor {
    private HashMap<Player, Long> healCooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;


        if (args.length == 0) {
            if(healCooldown.containsKey(player)){
                if(player.hasPermission("citybuild.command.heal.self.cooldown.bypass"));
            }
            player.setHealth(20);
            player.sendMessage("§2Du wurdest geheilt");
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setRemainingAir(15 * 20);


        }
        return false;


    }
}