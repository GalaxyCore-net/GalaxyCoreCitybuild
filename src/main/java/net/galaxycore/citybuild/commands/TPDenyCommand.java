package net.galaxycore.citybuild.commands;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPDenyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!TPACommand.getTparequest().containsKey(player)) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.tpa.notfound"));
            return true;
        }
        TPACommand.getTparequest().remove(player);
        player.sendMessage(I18N.getByPlayer(player, "citybuild.tpa.deny"));
        return true;
    }
}
