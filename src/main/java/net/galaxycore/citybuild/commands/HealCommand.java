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

import java.util.HashMap;

public class HealCommand implements CommandExecutor {
    private HashMap<Player, Long> healCooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;


        if (args.length == 0) {
            if (healCooldown.containsKey(player)) {
                if (player.hasPermission("citybuild.command.heal.self.cooldown.bypass")) ;
                //To Do Cooldown mit Permission Bypass
            }
            player.setHealth(20);
            player.sendMessage(I18N.getByPlayer(player, "citybuild.heal.self"));
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setRemainingAir(15 * 20);

        } else if (args.length == 1) {

            if (!player.hasPermission("citybuild.command.heal.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            target.setHealth(20);
            target.setFoodLevel(20);
            target.setFireTicks(0);
            target.setRemainingAir(15 * 20);
            target.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.heal.other.self"), new LuckPermsApiWrapper(player)));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.heal.other"), new LuckPermsApiWrapper(target)));


        }
        return true;
    }
}
