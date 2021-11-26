package net.galaxycore.citybuild.commands;

import lombok.SneakyThrows;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TpaToggleCommand implements CommandExecutor {
    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("citybuild.command.tpatoggle")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }
        PreparedStatement statementIsPlayerLocked = Essential.getCore().getDatabaseConfiguration().getConnection().prepareStatement("SELECT tpatoggle FROM galaxycity_playerdb where ID = ? ");
        statementIsPlayerLocked.setInt(1, PlayerLoader.load(player).getId());
        PreparedStatement statementUpdatePlayer = Essential.getCore().getDatabaseConfiguration().getConnection().prepareStatement("UPDATE galaxycity_playerdb SET tpatoggle = ? where ID = ?");
        statementUpdatePlayer.setInt(2, PlayerLoader.load(player).getId());
        ResultSet resultIsPlayerLocked = statementIsPlayerLocked.executeQuery();
        if (resultIsPlayerLocked.next()) {
            if (resultIsPlayerLocked.getBoolean("tpatoggle")) {
                statementUpdatePlayer.setBoolean(1, false);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.tpatoggle.open"));
            } else {
                statementUpdatePlayer.setBoolean(1, true);
                player.sendMessage(I18N.getByPlayer(player, "citybuild.tpatoggle.close"));
            }
        }
        statementUpdatePlayer.executeUpdate();
        statementUpdatePlayer.close();
        resultIsPlayerLocked.close();
        statementIsPlayerLocked.close();
        return true;
    }
}
