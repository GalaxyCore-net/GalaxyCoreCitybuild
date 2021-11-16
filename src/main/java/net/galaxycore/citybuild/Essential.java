package net.galaxycore.citybuild;

import net.galaxycore.citybuild.commands.*;
import net.galaxycore.citybuild.listeners.DamageListener;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Essential extends JavaPlugin {
    public static GalaxyCoreCore core;

    @Override
    public void onEnable() {

        core = getServer().getServicesManager().load(GalaxyCoreCore.class);

        I18N.setDefaultByLang("de_DE", "citybuild.noperms", "§cDu hast keine Berechtigung für diesen Command.");
        I18N.setDefaultByLang("de_DE", "citybuild.noplayerfound", "§cDieser Spieler ist nicht online");
        I18N.setDefaultByLang("de_DE", "citybuild.creative", "§cDu bist nun im Kreativmodus");
        I18N.setDefaultByLang("de_DE", "citybuild.creative.set", "§cDu hast %player% in den Kreativmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.survival", "§cDu bist nun im Survivalmodus");
        I18N.setDefaultByLang("de_DE", "citybuild.survival.set", "§cDu hast %player% in den Suvivalmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.spectator", "§cDu bist nun im Spectatormodus");
        I18N.setDefaultByLang("de_DE", "citybuild.spectator.set", "§cDu hast %player% in den Spectatormodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.adventure", "§cDu bist nun im Adventuremodus");
        I18N.setDefaultByLang("de_DE", "citybuild.adventure.set", "§cDu hast %player% in den Adventuremodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.gamemode.usage", "§cBenutze: /gamemode <0|1|2|3>");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.on", "§cDu wurdest in den FlugModus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.off", "§cDu wurdest aus dem FlugModus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.on.other", "§cDu wurdest von %player% in den Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.off.other", "§cDu wurdest von %player% aus dem Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.set.on", "§cDu hast %player% in den Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.set.off", "§cDu hast %player% aus dem Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.feed", "§cDeine Hungerleiste wurde aufgefüllt");
        I18N.setDefaultByLang("de_DE", "citybuild.feed.other", "§cDeine Hungerleiste wurde von %player% aufgefüllt");
        I18N.setDefaultByLang("de_DE", "citybuild.feed.other.notify", "§cDu hast %player%´s Hungerleiste aufgefüllt");
        I18N.setDefaultByLang("de_DE", "citybuild.heal.self", "§cDu hast dich geheilt");
        I18N.setDefaultByLang("de_DE", "citybuild.heal.other", "§cDu hast %player% geheilt");
        I18N.setDefaultByLang("de_DE", "citybuild.heal.other.self", "§cDu wurdest von %player% geheilt");
        I18N.setDefaultByLang("de_DE", "citybuild.debug", "§cSollte jetzt eigentlich funktionieren %player%");
        I18N.setDefaultByLang("de_DE", "citybuild.time.day", "§cDu hast die Zeit auf Tag gesetzt ");
        I18N.setDefaultByLang("de_DE", "citybuild.time.night", "§cDu hast die Zeit auf Nacht gesetzt ");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.clear", "§cDu hast das Wetter auf Klar geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.rain", "§cDu hast das Wetter auf Regen geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.thunder", "§cDu hast das Wetter auf Gewitter geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.cooldown", "§cDu musst noch bis %time% warten");
        I18N.setDefaultByLang("de_DE", "citybuild.hat", "§cViel Spaß mit deinem neuen Hut!");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.clear", "§c%player% hat das Wetter auf Klar geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.rain", "§c%player% hat das Wetter auf Regen geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.thunder", "§c%player% hat das Wetter auf Gewitter geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.day.global", "§c%player% hat die Zeit auf Tag verändert");
        I18N.setDefaultByLang("de_DE", "citybuild.night.global", "§c%player% hat die Zeit auf Nacht verändert");
        I18N.setDefaultByLang("de_DE", "citybuild.god.on", "§cDu hast dich in den Gottmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.god.off", "§cDu hast dich aus dem Gottmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.god.on.other", "§c%player% hat dich in den Gottmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.god.on.other.notify", "§cDu hast %player% in den Gottmodus");
        I18N.setDefaultByLang("de_DE", "citybuild.god.off.other", "§c%player% hat dich in den Gottmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.god.off.other.notify", "§cDu hast %player% in den Gottmodus");
        I18N.setDefaultByLang("de_DE", "citybuild.god.usage", "§cBenutze: /god Player");

        I18N.setDefaultByLang("en_GB", "citybuild.noperms", "§cYou're not permitted to use this");
        I18N.setDefaultByLang("en_GB", "citybuild.noplayerfound", "§cThis Player isn't online");
        I18N.setDefaultByLang("en_GB", "citybuild.creative", "§cYour Gamemode has been set to Creative Mode ");
        I18N.setDefaultByLang("en_GB", "citybuild.creative.set", "§cYou have set %player% to Creative Mode");
        I18N.setDefaultByLang("en_GB", "citybuild.survival", "§cYour Gamemode has been set to Survival Mode ");
        I18N.setDefaultByLang("en_GB", "citybuild.survival.set", "§cYou have set %player% to Survival Mode");
        I18N.setDefaultByLang("en_GB", "citybuild.spectator", "§cYour Gamemode has been set to Spectator Mode ");
        I18N.setDefaultByLang("en_GB", "citybuild.spectator.set", "§cYou have set %player% to Spectator Mode");
        I18N.setDefaultByLang("en_GB", "citybuild.adventure", "§cYour Gamemode has been set to Adventure Mode ");
        I18N.setDefaultByLang("en_GB", "citybuild.adventure.set", "§cYou have set %player% to Adventure Mode");
        I18N.setDefaultByLang("en_GB", "citybuild.gamemode.usage", "§cUsage: /gamemode <0|1|2|3>");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.on", "§cYou have been set to flight mode.");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.off", "§cYou have been put out of flight mode");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.on.other", "§cYou have been put into flight mode by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.off.other", "§cYou have been put out of flight mode by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.set.on", "§cYou have set %player% to flight mode");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.set.off", "§cYou have set %player% out of flight mode");
        I18N.setDefaultByLang("en_GB", "citybuild.feed", "§cYour hunger bar has been filled up");
        I18N.setDefaultByLang("en_GB", "citybuild.feed.other", "§cYour hunger bar has been filled up by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.feed.other.notify", "§cYou have filled up %player%'s hunger bar");
        I18N.setDefaultByLang("en_GB", "citybuild.heal.self", "§cYou have healed yourself");
        I18N.setDefaultByLang("en_GB", "citybuild.heal.other", "§cYou have healed %player");
        I18N.setDefaultByLang("en_GB", "citybuild.heal.other.self", "§cYou have been healed by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.debug", "§cShould actually work now %player%");
        I18N.setDefaultByLang("en_GB", "citybuild.time.day", "§cYou have set the time to day ");
        I18N.setDefaultByLang("en_GB", "citybuild.time.night", "§cYou have set the time to night ");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.clear", "§cYou changed the weather to clear");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.rain", "§cYou have changed the weather to rain");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.thunder", "§cYou have changed the weather to thunderstorms");
        I18N.setDefaultByLang("en_GB", "citybuild.cooldown", "§cYou still have to wait until %time%.");
        I18N.setDefaultByLang("en_GB", "citybuild.hat", "§cHave fun with your new Hat!");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.clear", "§c%player% changed the weather to clear");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.rain", "§c%player% changed the weather to rain");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.thunder", "§c%player% changed the weather to thunder");
        I18N.setDefaultByLang("en_GB", "citybuild.day.global", "§c%player% has changed the time to day");
        I18N.setDefaultByLang("en_GB", "citybuild.night.global", "§c%player% has changed the time to night");
        I18N.setDefaultByLang("en_GB", "citybuild.god.on", "§cYou have put yourself in God mode");
        I18N.setDefaultByLang("en_GB", "citybuild.god.off", "§cYou have put yourself out of God mode");
        I18N.setDefaultByLang("en_GB", "citybuild.god.on.other", "§c%player% has put you in god mode");
        I18N.setDefaultByLang("en_GB", "citybuild.god.on.other.notify", "§cYou have set %player% to god mode.");
        I18N.setDefaultByLang("en_GB", "citybuild.god.off.other", "§c%player% has put you in god mode");
        I18N.setDefaultByLang("en_GB", "citybuild.god.off.other.notify", "§cYou have set %player% to god mode.");
        I18N.setDefaultByLang("en_GB", "citybuild.god.usage", "§cUsage: /god Player");



        Objects.requireNonNull(getCommand("debug")).setExecutor(new DebugCommand());
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new GamemodeCommand());
        Objects.requireNonNull(getCommand("heal")).setExecutor(new HealCommand());
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand("feed")).setExecutor(new FeedCommand());
        Objects.requireNonNull(getCommand("workbench")).setExecutor(new WorkbenchCommand());
        Objects.requireNonNull(getCommand("anvil")).setExecutor(new AnvilCommand());
        Objects.requireNonNull(getCommand("day")).setExecutor(new DayCommand());
        Objects.requireNonNull(getCommand("night")).setExecutor(new NightCommand());
        Objects.requireNonNull(getCommand("hat")).setExecutor(new HatCommand());
        Objects.requireNonNull(getCommand("weather")).setExecutor(new WeatherCommand());
        Objects.requireNonNull(getCommand("god")).setExecutor(new GodCommand());

        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
