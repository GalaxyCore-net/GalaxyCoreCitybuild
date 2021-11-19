package net.galaxycore.citybuild.listeners;

import net.galaxycore.citybuild.commands.EnderChestCommand;
import net.galaxycore.citybuild.commands.InvseeCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerClickEventListener implements Listener {

    @EventHandler
    public void onPlayerClickEvent(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            if (EnderChestCommand.clickEvent.contains(((Player) event.getWhoClicked()))) {
                event.setCancelled(true);
            }
            if (InvseeCommand.clickEvent.contains(((Player) event.getWhoClicked()))) {
                event.setCancelled(true);
            }
        }

    }
}