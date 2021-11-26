package net.galaxycore.citybuild.commands;

import net.galaxycore.citybuild.utils.TpaRequest;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPAAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if(!player.hasPermission("citybuild.tpaall")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            TPACommand.getTparequest().put(onlinePlayer, new TpaRequest(onlinePlayer, player, true, System.currentTimeMillis()));
            onlinePlayer.sendMessage(Component.text(StringUtils.replaceRelevant(I18N.getByPlayer(onlinePlayer, "citybuild.tpahere.message1"), new LuckPermsApiWrapper(player))).append(Component.newline()).append(Component.text(I18N.getByPlayer(onlinePlayer, "citybuild.tpa.yes")).clickEvent(ClickEvent.runCommand("/tpaccept"))).append(Component.text(" ")).append(Component.text(I18N.getByPlayer(onlinePlayer, "citybuild.tpa.no")).clickEvent(ClickEvent.runCommand("/tpdeny"))));
        }

        player.sendMessage(I18N.getByPlayer(player, "citybuild.tpaall"));

        return true;
    }
}
