package net.galaxycore.essential;

import net.galaxycore.essential.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Essential extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("Debug").setExecutor(new DebugCommand());
        getCommand("Gamemode").setExecutor(new GamemodeCommand());
        getCommand("Heal").setExecutor(new HealCommand());
        getCommand("Fly").setExecutor(new FlyCommand());
        getCommand("Feed").setExecutor(new FeedCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
