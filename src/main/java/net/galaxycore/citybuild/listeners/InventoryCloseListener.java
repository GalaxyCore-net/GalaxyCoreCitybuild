package net.galaxycore.citybuild.listeners;

import net.galaxycore.citybuild.commands.EnderChestCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onQuit(InventoryCloseEvent event) {
        EnderChestCommand.clickEvent.remove((Player) event.getPlayer());

    }
}