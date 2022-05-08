package net.galaxycore.citybuild.commands;

import lombok.SneakyThrows;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.citybuild.pmenu.utils.Warp;
import net.galaxycore.citybuild.utils.FastUtils;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SetWarpCommand implements CommandExecutor {
    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("citybuild.command.setwarp")) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.noperms"));
            return true;
        }

        if (args.length != 2 || player.getInventory().getItemInMainHand().getType() == Material.AIR || FastUtils.isNegativeInt(args[1])) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.setwarp.usage"));
            return true;
        }

        int pos = Integer.parseInt(args[1]);

        if (pos > 8) {
            player.sendMessage(I18N.getByPlayer(player, "citybuild.setwarp.usage"));
            return true;
        }

        Warp warp = new Warp(player.getLocation(), args[0].replace("&", "§"), pos, player.getInventory().getItemInMainHand().getType());

        Connection connection = Essential.getCore().getDatabaseConfiguration().getConnection();
        ResultSet resultSet = connection.prepareStatement("SELECT * FROM galaxycity_warps").executeQuery();
        Warp[] mapping = new Warp[9];

        while (resultSet.next()) Warp.map(resultSet, mapping);

        resultSet.close();

        mapping[pos] = warp;

        connection.prepareStatement("DELETE FROM galaxycity_warps WHERE 1").executeUpdate();

        for (Warp myWarp : mapping) {
            if(myWarp == null) continue;
            PreparedStatement statement = connection.prepareStatement("INSERT INTO galaxycity_warps (pos, loc, name, display) VALUES (?, ?, ?, ?)");
            statement.setInt(1, myWarp.getPos());
            statement.setString(2, serializeLocation(myWarp.getTarget()));
            statement.setString(3, myWarp.getName());
            statement.setString(4, myWarp.getDisplay().toString());
            statement.executeUpdate();
        }

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 1);

        return true;
    }

    private String serializeLocation(Location location) {
        return location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ();
    }
}
