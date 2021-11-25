package net.galaxycore.citybuild.commands;

import lombok.SneakyThrows;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.utils.TpaRequest;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TPAHereCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.tpahere")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            PreparedStatement statementIsPlayerLocked = Essential.getCore().getDatabaseConfiguration().getConnection().prepareStatement("SELECT tpatoggle FROM galaxycity_playerdb where ID = ? ");
            statementIsPlayerLocked.setInt(1, PlayerLoader.load(target).getId());
            ResultSet resultIsPlayerLocked = statementIsPlayerLocked.executeQuery();
            if (resultIsPlayerLocked.next()) {
                if (resultIsPlayerLocked.getBoolean("tpatoggle")) {
                    player.sendMessage((I18N.getByPlayer(player, "citybuild.tpatoggle")));
                    resultIsPlayerLocked.close();
                    statementIsPlayerLocked.close();
                    return true;
                }
                resultIsPlayerLocked.close();
                statementIsPlayerLocked.close();
            }
            TPACommand.getTparequest().put(target, new TpaRequest(target, player, false));
            player.sendMessage(StringUtils.replaceRelevant(I18N.getByPlayer(player, "citybuild.tpahere.message2"), new LuckPermsApiWrapper(target)));
            target.sendMessage(Component.text(StringUtils.replaceRelevant(I18N.getByPlayer(target, "citybuild.tpahere.message1"), new LuckPermsApiWrapper(player))).append(Component.newline()).append(Component.text(I18N.getByPlayer(target, "citybuild.tpa.yes")).clickEvent(ClickEvent.runCommand("/tpaccept"))).append(Component.text(" ")).append(Component.text(I18N.getByPlayer(target, "citybuild.tpa.no")).clickEvent(ClickEvent.runCommand("/tpdeny"))));
        } else {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.tpahere.usage"));
        }
        return true;
    }

}