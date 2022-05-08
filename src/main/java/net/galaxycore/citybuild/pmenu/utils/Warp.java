package net.galaxycore.citybuild.pmenu.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class Warp {
    private final Location target;
    private final String name;
    private final int pos;
    private final Material display;


    public static void map(ResultSet resultSet, Warp[] warps) throws SQLException {
        Location target = parseLocation(resultSet.getString("loc"));
        String name = resultSet.getString("name");
        int pos = resultSet.getInt("pos");
        Material display = Material.getMaterial(resultSet.getString("display"));

        warps[pos] = new Warp(target, name, pos, display);
    }

    private static Location parseLocation(String loc) {
        String[] split = loc.split("\\|");
        return Objects.requireNonNull(Bukkit.getWorld(split[0])).getBlockAt(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])).getLocation();
    }
}
