package net.galaxycore.citybuild.listeners;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.galaxycore.citybuild.Essential;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

@Getter
public class PlayerJoinListener implements Listener {
    @Getter
    private static Location spawnLoc;
    private final String spawn_world;
    private final int spawn_x;
    private final int spawn_y;
    private final int spawn_z;
    private final float spawn_yaw;
    private final float spawn_pitch;

    public PlayerJoinListener(@NonNull ConfigNamespace configNamespace) {
        spawn_world = configNamespace.get("spawn.world");

        spawn_x = Integer.parseInt(configNamespace.get("spawn.x"));
        spawn_y = Integer.parseInt(configNamespace.get("spawn.y"));
        spawn_z = Integer.parseInt(configNamespace.get("spawn.z"));
        spawn_yaw = Float.parseFloat(configNamespace.get("spawn.yaw"));
        spawn_pitch = Float.parseFloat(configNamespace.get("spawn.pitch"));

        spawnLoc = getSpawn();
    }

    @SneakyThrows
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setGameMode(GameMode.SURVIVAL);

        player.teleport(spawnLoc);
        // Every unregistered Player cannot be loaded here
        if (PlayerLoader.load(player) == null) return;
        PreparedStatement statementIsPlayerInDatabase = Essential.getCore().getDatabaseConfiguration().getConnection().prepareStatement("SELECT ID FROM galaxycity_playerdb where ID = ? ");
        statementIsPlayerInDatabase.setInt(1, PlayerLoader.load(player).getId());
        ResultSet resultIsPlayerInDatabase = statementIsPlayerInDatabase.executeQuery();
        if (!resultIsPlayerInDatabase.next()) {
            PreparedStatement statementAddPlayerToDatabase = Essential.getCore().getDatabaseConfiguration().getConnection().prepareStatement("INSERT INTO galaxycity_playerdb (ID) VALUES (?)");
            statementAddPlayerToDatabase.setInt(1, PlayerLoader.load(player).getId());
            statementAddPlayerToDatabase.executeUpdate();
            statementAddPlayerToDatabase.close();
        }
        resultIsPlayerInDatabase.close();
        statementIsPlayerInDatabase.close();
    }

    private Location getSpawn() {
        Location spawn = Objects.requireNonNull(Bukkit.getWorld(spawn_world)).getBlockAt(spawn_x, spawn_y, spawn_z).getLocation();

        spawn.add(0.5, 0.01, 0.5);

        spawn.setYaw(spawn_yaw);
        spawn.setPitch(spawn_pitch);

        return spawn;
    }
}