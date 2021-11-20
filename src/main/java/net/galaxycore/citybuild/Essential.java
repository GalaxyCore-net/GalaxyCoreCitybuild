package net.galaxycore.citybuild;

import lombok.SneakyThrows;
import net.galaxycore.citybuild.commands.*;
import net.galaxycore.citybuild.listeners.*;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Essential extends JavaPlugin {
    private static Essential instance;
    private static GalaxyCoreCore core;
    private ConfigNamespace configNamespace;

    public static Essential getInstance() {
        return instance;
    }

    public static void setInstance(Essential instance) {
        Essential.instance = instance;
    }

    public static GalaxyCoreCore getCore() {
        return core;
    }

    public static void setCore(GalaxyCoreCore core) {
        Essential.core = core;
    }

    public ConfigNamespace getConfigNamespace() {
        return configNamespace;
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        setInstance(this);
        setCore(getServer().getServicesManager().load(GalaxyCoreCore.class));

        getCore().getDatabaseConfiguration().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS galaxycity_playerdb (ID int,invtoggle bit default 0,ectoggle bit default 0,tptoggle bit default 0,tpatoggle bit default 0);").executeUpdate();

        configNamespace = core.getDatabaseConfiguration().getNamespace("galaxycity");
        configNamespace.setDefault("spawn.world", "world");
        configNamespace.setDefault("spawn.x", "186");
        configNamespace.setDefault("spawn.y", "65");
        configNamespace.setDefault("spawn.z", "-278");
        configNamespace.setDefault("spawn.yaw", "-90");
        configNamespace.setDefault("spawn.pitch", "0");

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
        I18N.setDefaultByLang("de_DE", "citybuild.fly.on", "§cDu wurdest in den Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.off", "§cDu wurdest aus dem Flugmodus gesetzt");
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
        I18N.setDefaultByLang("de_DE", "citybuild.tp.self", "§cDu hast dich zu %player% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpo.self", "§cDu hast dich zu %player% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.other", "§c%player% hat dich zu dir teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target.tp.notify", "§cDu hast %target1% zu %target2% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpo.target.tpo.notify", "§cDu hast %target1% zu %target2% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target1", "§c%target2% wurde von %player% zu dir teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target2", "§cDu wurdest von %player% zu %target1% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.self", "§cDu hast dich zum Spawn teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.other", "§cDu wurdest von %player% zum Spawn teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.other.notify", "§cDu hast %player% zum Spawn teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.request", "§cDu kannst dir nicht selber eine Anfrage schicken!");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.accept", "§c%player% hat deine Anfrage angenommen und wird in 5 Sekunden zu dir teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.deny", "§c%player% hat deine Anfrage abgelehnt.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.load", "§cTeleportiere ...");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.self", "§cDu hast dich in den Speedmodus 1 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.self", "§cDu hast dich in den Speedmodus 2 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.self", "§cDu hast dich in den Speedmodus 3 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.self", "§cDu hast dich in den Speedmodus 4 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.self", "§cDu hast dich in den Speedmodus 5 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.self", "§cDu hast dich in den Speedmodus 6 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.self", "§cDu hast dich in den Speedmodus 7 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.self", "§cDu hast dich in den Speedmodus 8 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.self", "§cDu hast dich in den Speedmodus 9 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.self", "§cDu hast dich in den Speedmodus 10 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.other", "§cDu wurdest von %player% in den Speedmodus 1 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.other.notify", "§cDu hast %player% in den Speedmodus 1 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.other", "§cDu wurdest von %player% in den Speedmodus 2 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.other.notify", "§cDu hast %player% in den Speedmodus 2 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.other", "§cDu wurdest von %player% in den Speedmodus 3 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.other.notify", "§cDu hast %player% in den Speedmodus 3 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.other", "§cDu wurdest von %player% in den Speedmodus 4 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.other.notify", "§cDu hast %player% in den Speedmodus 4 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.other", "§cDu wurdest von %player% in den Speedmodus 5 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.other.notify", "§cDu hast %player% in den Speedmodus 5 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.other", "§cDu wurdest von %player% in den Speedmodus 6 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.other.notify", "§cDu hast %player% in den Speedmodus 6 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.other", "§cDu wurdest von %player% in den Speedmodus 7 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.other.notify", "§cDu hast %player% in den Speedmodus 7 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.other", "§cDu wurdest von %player% in den Speedmodus 8 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.other.notify", "§cDu hast %player% in den Speedmodus 8 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.other", "§cDu wurdest von %player% in den Speedmodus 9 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.other.notify", "§cDu hast %player% in den Speedmodus 9 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.other", "§cDu wurdest von %player% in den Speedmodus 10 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.other.notify", "§cDu hast %player% in den Speedmodus 10 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.ectoggle", "§cDiese Enderchest ist gesperrt");
        I18N.setDefaultByLang("de_DE", "citybuild.invtoggle", "§cDieses Inventar ist gesperrt");
        I18N.setDefaultByLang("de_DE", "citybuild.enderchest.open", "§cDeine Enderchest ist nun wieder offen.");
        I18N.setDefaultByLang("de_DE", "citybuild.enderchest.open", "§cDeine Enderchest ist nun geschlossen.");
        I18N.setDefaultByLang("de_DE", "citybuild.inventar.close", "§cDein Inventar ist nun geschlossen.");
        I18N.setDefaultByLang("de_DE", "citybuild.inventar.open", "§cDein Inventar ist nun offen.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpoall", "§cDu hast alle zu dir Teleportiert.");

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
        I18N.setDefaultByLang("en_GB", "citybuild.tp.self", "§cYou have teleported to %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpo.self", "§cYou have teleported to %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.tp.other", "§c%player% teleported you to you.");
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target.tp.notify", "§cYou teleported %target1% to %target2%."); // Command Executor
        I18N.setDefaultByLang("en_GB", "citybuild.tpo.target.tpo.notify", "§cYou teleported %target1% to %target2%."); // Command Executor
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target1", "§%target2% was teleported to you by %player%."); // Player to you
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target2", "§cYou have been teleported from %player% to %target1%."); // you to Player
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.self", "§cYou have teleported to the spawn.");
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.other", "§cYou have been teleported to the spawn by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.other.notify", "§cYou %target% teleported to the spawn.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.request", "§cYou can't send yourself a request!");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.accept", "§c%player% has accepted your request and will teleport to you in 3 seconds.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.deny", "§c%player% has rejected your request.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.load", "§cTeleporting...");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.self", "§cYou have set yourself to speed mode 1");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.self", "§cYou have set yourself to speed mode 2");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.self", "§cYou have set yourself to speed mode 3");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.self", "§cYou have set yourself to speed mode 4");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.self", "§cYou have set yourself to speed mode 5");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.self", "§cYou have set yourself to speed mode 6");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.self", "§cYou have set yourself to speed mode 7");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.self", "§cYou have set yourself to speed mode 8");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.self", "§cYou have set yourself to speed mode 9");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.self", "§cYou have set yourself to speed mode 10");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.other", "§cYou have been set to speed mode 1 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.other.notify", "§cYou have set %player% to speed mode 1.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.other", "§cYou have been set to speed mode 2 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.other.notify", "§cYou have set %player% to speed mode 2.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.other", "§cYou have been set to speed mode 3 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.other.notify", "§cYou have set %player% to speed mode 3.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.other", "§cYou have been set to speed mode 4 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.other.notify", "§cYou have set %player% to speed mode 4.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.other", "§cYou have been set to speed mode 5 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.other.notify", "§cYou have set %player% to speed mode 5.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.other", "§cYou have been set to speed mode 6 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.other.notify", "§cYou have set %player% to speed mode 6.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.other", "§cYou have been set to speed mode 7 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.other.notify", "§cYou have set %player% to speed mode 7.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.other", "§cYou have been set to speed mode 8 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.other.notify", "§cYou have set %player% to speed mode 8.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.other", "§cYou have been set to speed mode 9 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.other.notify", "§cYou have set %player% to speed mode 9.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.other", "§cYou have been set to speed mode 10 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.other.notify", "§cYou have set %player% to speed mode 10.");
        I18N.setDefaultByLang("en_GB", "citybuild.ectoggle", "§cDiese Enderchest ist gesperrt");
        I18N.setDefaultByLang("en_GB", "citybuild.invtoggle", "§cDieses Inventar ist gesperrt");
        I18N.setDefaultByLang("en_GB", "citybuild.enderchest.open", "§cDeine Enderchest ist nun wieder offen.");
        I18N.setDefaultByLang("en_GB", "citybuild.enderchest.open", "§cDeine Enderchest ist nun geschlossen.");
        I18N.setDefaultByLang("en_GB", "citybuild.inventar.close", "§cDein Inventar ist nun geschlossen.");
        I18N.setDefaultByLang("en_GB", "citybuild.inventar.open", "§cDein Inventar ist nun offen.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpoall", "§cDu hast alle zu dir Teleportiert.");

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
        Objects.requireNonNull(getCommand("teleport")).setExecutor(new TeleportCommand());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());
        Objects.requireNonNull(getCommand("testcommand")).setExecutor(new TestCommand());
        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderChestCommand());
        Objects.requireNonNull(getCommand("invsee")).setExecutor(new InvseeCommand());
        Objects.requireNonNull(getCommand("speed")).setExecutor(new SpeedCommand());
        Objects.requireNonNull(getCommand("ectoggle")).setExecutor(new EcToggleCommand());
        Objects.requireNonNull(getCommand("invtoggle")).setExecutor(new InvToggleCommand());
        Objects.requireNonNull(getCommand("skull")).setExecutor(new SkullCommand());
        Objects.requireNonNull(getCommand("tpo")).setExecutor(new TpoCommand());
        Objects.requireNonNull(getCommand("tpoall")).setExecutor(new TpoAllCommand());

        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(getConfigNamespace()), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
