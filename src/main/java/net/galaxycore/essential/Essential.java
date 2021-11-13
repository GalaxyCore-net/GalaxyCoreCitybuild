package net.galaxycore.essential;

import net.galaxycore.essential.commands.DebugCommand;
import net.galaxycore.essential.commands.GamemodeCommand;
import net.galaxycore.essential.commands.HealCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Essential extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand( "Debug").setExecutor(new DebugCommand());
        getCommand( "Gamemode").setExecutor(new GamemodeCommand());
        getCommand("Heal").setExecutor(new HealCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
