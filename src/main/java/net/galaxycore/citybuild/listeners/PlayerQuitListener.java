package net.galaxycore.citybuild.listeners;

import net.galaxycore.citybuild.commands.EnderChestCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        EnderChestCommand.clickEvent.remove(event.getPlayer());

    }
}
