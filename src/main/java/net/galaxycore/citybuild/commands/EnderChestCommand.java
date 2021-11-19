package net.galaxycore.citybuild.commands;

import lombok.SneakyThrows;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EnderChestCommand implements CommandExecutor {
    public static ArrayList<Player> clickEvent = new ArrayList<>();

    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            if (!player.hasPermission("citybuild.command.enderchest")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            player.openInventory(player.getEnderChest());
        }
        if (args.length == 1) {
            if (!player.hasPermission("citybuild.command.enderchest.other")) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(I18N.getByPlayer(player, "citybuild.noplayerfound"));
                return true;
            }
            if (!player.hasPermission("citybuild.enderchest.toggle.bypass")) {
                PreparedStatement statementIsPlayerLocked = Essential.getCore().getDatabaseConfiguration().getConnection().prepareStatement("SELECT ectoggle FROM galaxycity_playerdb where ID = ? ");
                statementIsPlayerLocked.setInt(1, PlayerLoader.load(target).getId());
                ResultSet resultIsPlayerLocked = statementIsPlayerLocked.executeQuery();
                if (resultIsPlayerLocked.next()) {
                    if (resultIsPlayerLocked.getBoolean("ectoggle")) {
                        player.sendMessage((I18N.getByPlayer(player, "citybuild.ectoggle")));
                        resultIsPlayerLocked.close();
                        statementIsPlayerLocked.close();
                        return true;
                    }
                }
                resultIsPlayerLocked.close();
                statementIsPlayerLocked.close();
            }
            player.openInventory(target.getEnderChest());
            if (!player.hasPermission("citybuild.command.enderchest.other.modify") || (target.hasPermission("citybuild.command.enderchest.other.modify.block") && !player.hasPermission("citybuild.command.enderchest.other.modify.block.bypass")))
                clickEvent.add(player);

        }

        return true;
    }
}