package net.galaxycore.citybuild.commands;

import net.galaxycore.citybuild.utils.Skull;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SkullCommand implements CommandExecutor {
    private final HashMap<Player, Long> skullCooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.skull")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            if (skullCooldown.containsKey(player)) {
                if (!player.hasPermission("citybuild.command.skull.cooldown.bypass")) {
                    if (skullCooldown.get(player) < System.currentTimeMillis()) {
                        skullCooldown.remove(player);
                    } else {
                        player.sendMessage(I18N.getByPlayer(player, "citybuild.cooldown")
                                .replace("%time%", new SimpleDateFormat(
                                        I18N.getInstanceRef().get().getLanguages()
                                                .get(I18N.getInstanceRef().get().getLocale(player)).getDateFormat() + " " +
                                                I18N.getInstanceRef().get().getLanguages()
                                                        .get(I18N.getInstanceRef().get().getLocale(player)).getTimeFormat())
                                        .format(new Date(skullCooldown.get(player)))));
                        return true;
                    }
                }
            }
            player.getInventory().addItem(Skull.getPlayerSkull(args[0]));
            skullCooldown.put(player, System.currentTimeMillis() + 604800000); // 604.800000ms = 7T
        }
        return true;
    }
}
