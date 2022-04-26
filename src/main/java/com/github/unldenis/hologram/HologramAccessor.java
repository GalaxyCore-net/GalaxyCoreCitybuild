package com.github.unldenis.hologram;

import org.bukkit.entity.Player;

// Protected? SIKE
public class HologramAccessor {
    public static void hide(Hologram hologram, Player player) {
        hologram.hide(player);
    }
    public static boolean isHidden(Hologram hologram, Player player) {
        return !hologram.getSeeingPlayers().contains(player);
    }
}
