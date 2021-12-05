package net.galaxycore.citybuild.listeners;

import net.galaxycore.citybuild.commands.GodCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity()instanceof Player){
            if (GodCommand.godMode.contains(((Player) event.getEntity()))){
                event.setCancelled(true);
            }
        }

    }
}
