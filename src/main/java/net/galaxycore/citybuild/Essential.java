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

        I18N.setDefaultByLang("de_DE", "citybuild.noperms", "§7Du hast keine Berechtigung für diesen Command.");
        I18N.setDefaultByLang("de_DE", "citybuild.noplayerfound", "§7Dieser Spieler ist nicht online");
        I18N.setDefaultByLang("de_DE", "citybuild.creative", "§7Du bist nun im Kreativmodus");
        I18N.setDefaultByLang("de_DE", "citybuild.creative.set", "§7Du hast %player% in den Kreativmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.survival", "§7Du bist nun im Survivalmodus");
        I18N.setDefaultByLang("de_DE", "citybuild.survival.set", "§7Du hast %player% in den Suvivalmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.spectator", "§7Du bist nun im Spectatormodus");
        I18N.setDefaultByLang("de_DE", "citybuild.spectator.set", "§7Du hast %player% in den Spectatormodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.adventure", "§7Du bist nun im Adventuremodus");
        I18N.setDefaultByLang("de_DE", "citybuild.adventure.set", "§7Du hast %player% in den Adventuremodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.gamemode.usage", "§7Benutze: /gamemode <0|1|2|3>");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.on", "§7Du wurdest in den Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.off", "§7Du wurdest aus dem Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.on.other", "§7Du wurdest von %player% in den Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.off.other", "§7Du wurdest von %player% aus dem Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.set.on", "§7Du hast %player% in den Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.set.off", "§7Du hast %player% aus dem Flugmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.feed", "§7Deine Hungerleiste wurde aufgefüllt");
        I18N.setDefaultByLang("de_DE", "citybuild.feed.other", "§7Deine Hungerleiste wurde von %player% aufgefüllt");
        I18N.setDefaultByLang("de_DE", "citybuild.feed.other.notify", "§7Du hast %player%´s Hungerleiste aufgefüllt");
        I18N.setDefaultByLang("de_DE", "citybuild.heal.self", "§7Du hast dich geheilt");
        I18N.setDefaultByLang("de_DE", "citybuild.heal.other", "§7Du hast %player% geheilt");
        I18N.setDefaultByLang("de_DE", "citybuild.heal.other.self", "§7Du wurdest von %player% geheilt");
        I18N.setDefaultByLang("de_DE", "citybuild.debug", "§7Sollte jetzt eigentlich funktionieren %player%");
        I18N.setDefaultByLang("de_DE", "citybuild.time.day", "§7Du hast die Zeit auf Tag gesetzt ");
        I18N.setDefaultByLang("de_DE", "citybuild.time.night", "§7Du hast die Zeit auf Nacht gesetzt ");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.clear", "§7Du hast das Wetter auf Klar geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.rain", "§7Du hast das Wetter auf Regen geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.thunder", "§7Du hast das Wetter auf Gewitter geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.cooldown", "§7Du musst noch bis %time% warten");
        I18N.setDefaultByLang("de_DE", "citybuild.hat", "§7Viel Spaß mit deinem neuen Hut!");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.clear", "§7%player% hat das Wetter auf Klar geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.rain", "§7%player% hat das Wetter auf Regen geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.thunder", "§7%player% hat das Wetter auf Gewitter geändert");
        I18N.setDefaultByLang("de_DE", "citybuild.day.global", "§7%player% hat die Zeit auf Tag verändert");
        I18N.setDefaultByLang("de_DE", "citybuild.night.global", "§7%player% hat die Zeit auf Nacht verändert");
        I18N.setDefaultByLang("de_DE", "citybuild.god.on", "§7Du hast dich in den Gottmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.god.off", "§7Du hast dich aus dem Gottmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.god.on.other", "§7%player% hat dich in den Gottmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.god.on.other.notify", "§7Du hast %player% in den Gottmodus");
        I18N.setDefaultByLang("de_DE", "citybuild.god.off.other", "§7%player% hat dich in den Gottmodus gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.god.off.other.notify", "§7Du hast %player% in den Gottmodus");
        I18N.setDefaultByLang("de_DE", "citybuild.god.usage", "§7Benutze: /god Player");
        I18N.setDefaultByLang("de_DE", "citybuild.tptoggle", "§7Du darfst dich nicht zu diesem Spieler teleportieren.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.self", "§7Du hast dich zu %player% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpo.self", "§7Du hast dich zu %player% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.other", "§7%player% hat dich zu dir teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target.tp.notify", "§7Du hast %target1% zu %target2% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpo.target.tpo.notify", "§7Du hast %target1% zu %target2% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target1", "§7%target2% wurde von %player% zu dir teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target2", "§7Du wurdest von %player% zu %target1% teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.self", "§7Du hast dich zum Spawn teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.other", "§7Du wurdest von %player% zum Spawn teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.other.notify", "§7Du hast %player% zum Spawn teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.request", "§7Du kannst dir nicht selber eine Anfrage schicken!");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.accept", "§7%player% hat deine Anfrage angenommen und du wirst in 3 Sekunden teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.deny", "§7%player% hat deine Anfrage abgelehnt.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.notfound", "§7Diese Tpa wurde nicht gefunden");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.expired", "§7Diese Tpa ist nicht mehr §cgültig");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.load", "§7Teleportiere ...");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.deny", "§7Du hast die TPA-Anfrage abgelehnt");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.accept", "§7Du hast die TPA-Anfrage angenommen");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.usage", "§7Benutze: /tpa [Player]");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.message1", "§7%player% möchte sich zu dir teleportieren.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.message2", "§7Du hast %player% eine TPA Anfrage gesendet.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.self.accept", "§7%player% hat deine Anfrage angenommen und wird in 3 Sekunden zu dir teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.message1", "§7%player% hat dir eine Teleport-Anfrage gesendet. Möchtest du dich teleportieren?");
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.message2", "§7Du hast %player% eine TPAHere Anfrage gesendet.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.yes", "§aAkzeptieren");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.no", "§cAblehnen");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.self", "§7Du hast dich in den Speedmodus 1 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.self", "§7Du hast dich in den Speedmodus 2 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.self", "§7Du hast dich in den Speedmodus 3 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.self", "§7Du hast dich in den Speedmodus 4 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.self", "§7Du hast dich in den Speedmodus 5 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.self", "§7Du hast dich in den Speedmodus 6 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.self", "§7Du hast dich in den Speedmodus 7 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.self", "§7Du hast dich in den Speedmodus 8 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.self", "§7Du hast dich in den Speedmodus 9 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.self", "§7Du hast dich in den Speedmodus 10 gesetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.other", "§7Du wurdest von %player% in den Speedmodus 1 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.other.notify", "§7Du hast %player% in den Speedmodus 1 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.other", "§7Du wurdest von %player% in den Speedmodus 2 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.other.notify", "§7Du hast %player% in den Speedmodus 2 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.other", "§7Du wurdest von %player% in den Speedmodus 3 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.other.notify", "§7Du hast %player% in den Speedmodus 3 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.other", "§7Du wurdest von %player% in den Speedmodus 4 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.other.notify", "§7Du hast %player% in den Speedmodus 4 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.other", "§7Du wurdest von %player% in den Speedmodus 5 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.other.notify", "§7Du hast %player% in den Speedmodus 5 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.other", "§7Du wurdest von %player% in den Speedmodus 6 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.other.notify", "§7Du hast %player% in den Speedmodus 6 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.other", "§7Du wurdest von %player% in den Speedmodus 7 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.other.notify", "§7Du hast %player% in den Speedmodus 7 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.other", "§7Du wurdest von %player% in den Speedmodus 8 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.other.notify", "§7Du hast %player% in den Speedmodus 8 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.other", "§7Du wurdest von %player% in den Speedmodus 9 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.other.notify", "§7Du hast %player% in den Speedmodus 9 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.other", "§7Du wurdest von %player% in den Speedmodus 10 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.other.notify", "§7Du hast %player% in den Speedmodus 10 gesetzt.");
        I18N.setDefaultByLang("de_DE", "citybuild.ectoggle", "§7Diese Enderchest ist gesperrt");
        I18N.setDefaultByLang("de_DE", "citybuild.invtoggle", "§7Dieses Inventar ist gesperrt");
        I18N.setDefaultByLang("de_DE", "citybuild.enderchest.open", "§7Deine Enderchest ist nun wieder offen.");
        I18N.setDefaultByLang("de_DE", "citybuild.enderchest.open", "§7Deine Enderchest ist nun geschlossen.");
        I18N.setDefaultByLang("de_DE", "citybuild.inventar.close", "§7Dein Inventar ist nun geschlossen.");
        I18N.setDefaultByLang("de_DE", "citybuild.inventar.open", "§7Dein Inventar ist nun offen.");
        I18N.setDefaultByLang("de_DE", "citybuild.inventar.notallow", "§7Du darfst dich nicht zu diesem Spieler teleportieren.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpoall", "§7Du hast alle zu dir Teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.ec.usage", "§7Benutze: §e/ec <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.feed.usage", "§7Benutze: §e/feed <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.fly.usage", "§7Benutze: §e/fly <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.god.usage", "§7Benutze: §e/god <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.heal.usage", "§7Benutze: §e/heal <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.invsee.usage", "§7Benutze: §e/invsee <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.skull.usage", "§7Benutze: §e/skull [Spieler]");
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.usage", "§7Benutze: §e/spawn <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.repair.usage", "§7Benutze: §e/spawn <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.usage", "§7Benutze: §e/speed [1-10] <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.teleport.usage", "§7Benutze: §e/teleport [Spieler] <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.tpo.usage", "§7Benutze: §e/tpo [Spieler] <Spieler>");
        I18N.setDefaultByLang("de_DE", "citybuild.weather.usage", "§7Benutze: §e/weather [clear|rain|thunder]");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.koordi.self", "§7Du hast dich zu den Koordinaten Teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.tp.koordi.other", "§7Du wurdest zu bestimmten Koordinaten Teleportiert.");
        I18N.setDefaultByLang("de_DE", "citybuild.repair.self", "§7Das Item in deiner Hand wurde repariert.");
        I18N.setDefaultByLang("de_DE", "citybuild.repair.other", "§7Das Item in deiner Hand wurde von %player% repariert.");
        I18N.setDefaultByLang("de_DE", "citybuild.repair.other.notify", "§7Du hast das Item von %player% repariert.");
        I18N.setDefaultByLang("de_DE", "citybuild.repair.fail", "§7Du kannst dieses Item nicht reparieren.");
        I18N.setDefaultByLang("de_DE", "citybuild.sign.success", "§7Dieses Item wurde erfolgreich signiert");
        I18N.setDefaultByLang("de_DE", "citybuild.sign.text", "§7Signiert von §e%player% §7am §e%time%");
        I18N.setDefaultByLang("de_DE", "citybuild.top.self", "§7Du wurdest auf den höchsten Block der Welt Teleportiert");
        I18N.setDefaultByLang("de_DE", "citybuild.top.other", "§7Du wurdest von %player% auf den höchsten Block der Welt Teleportiert");
        I18N.setDefaultByLang("de_DE", "citybuild.top.other.notify", "§7Du hast %player% auf den höchsten Block der Welt Teleportiert");
        I18N.setDefaultByLang("de_DE", "citybuild.top.usage", "§7Benutze: /top [Spieler]");
        I18N.setDefaultByLang("de_DE", "citybuild.tpatoggle", "§7Du darfst diesem Spieler keine TPA senden");
        I18N.setDefaultByLang("de_DE", "citybuild.tptoggle.open", "§7Ab jetzt kann man sich wieder zu dir teleportieren.");
        I18N.setDefaultByLang("de_DE", "citybuild.tptoggle.close", "§7Ab jetzt kann man sich nicht mehr zu dir teleportieren.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpatoggle.close", "§7Ab jetzt kann man dir keine Tpa-Anfrage mehr senden.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpatoggle.open", "§7Ab jetzt kann man dir wieder Tpa-Anfragen senden.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.usage", "§7Benutze: /tpahere [Player]");

        I18N.setDefaultByLang("en_GB", "citybuild.noperms", "§7You're not permitted to use this");
        I18N.setDefaultByLang("en_GB", "citybuild.noplayerfound", "§7This Player isn't online");
        I18N.setDefaultByLang("en_GB", "citybuild.creative", "§7Your Gamemode has been set to Creative Mode ");
        I18N.setDefaultByLang("en_GB", "citybuild.creative.set", "§7You have set %player% to Creative Mode");
        I18N.setDefaultByLang("en_GB", "citybuild.survival", "§7Your Gamemode has been set to Survival Mode ");
        I18N.setDefaultByLang("en_GB", "citybuild.survival.set", "§7You have set %player% to Survival Mode");
        I18N.setDefaultByLang("en_GB", "citybuild.spectator", "§7Your Gamemode has been set to Spectator Mode ");
        I18N.setDefaultByLang("en_GB", "citybuild.spectator.set", "§7You have set %player% to Spectator Mode");
        I18N.setDefaultByLang("en_GB", "citybuild.adventure", "§7Your Gamemode has been set to Adventure Mode ");
        I18N.setDefaultByLang("en_GB", "citybuild.adventure.set", "§7You have set %player% to Adventure Mode");
        I18N.setDefaultByLang("en_GB", "citybuild.gamemode.usage", "§7Usage: /gamemode <0|1|2|3>");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.on", "§7You have been set to flight mode.");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.off", "§7You have been put out of flight mode");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.on.other", "§7You have been put into flight mode by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.off.other", "§7You have been put out of flight mode by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.set.on", "§7You have set %player% to flight mode");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.set.off", "§7You have set %player% out of flight mode");
        I18N.setDefaultByLang("en_GB", "citybuild.feed", "§7Your hunger bar has been filled up");
        I18N.setDefaultByLang("en_GB", "citybuild.feed.other", "§7Your hunger bar has been filled up by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.feed.other.notify", "§7You have filled up %player%'s hunger bar");
        I18N.setDefaultByLang("en_GB", "citybuild.heal.self", "§7You have healed yourself");
        I18N.setDefaultByLang("en_GB", "citybuild.heal.other", "§7You have healed %player");
        I18N.setDefaultByLang("en_GB", "citybuild.heal.other.self", "§7You have been healed by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.debug", "§7Should actually work now %player%");
        I18N.setDefaultByLang("en_GB", "citybuild.time.day", "§7You have set the time to day ");
        I18N.setDefaultByLang("en_GB", "citybuild.time.night", "§7You have set the time to night ");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.clear", "§7You changed the weather to clear");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.rain", "§7You have changed the weather to rain");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.thunder", "§7You have changed the weather to thunderstorms");
        I18N.setDefaultByLang("en_GB", "citybuild.cooldown", "§7You still have to wait until %time%.");
        I18N.setDefaultByLang("en_GB", "citybuild.hat", "§7Have fun with your new Hat!");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.clear", "§7%player% changed the weather to clear");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.rain", "§7%player% changed the weather to rain");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.thunder", "§7%player% changed the weather to thunder");
        I18N.setDefaultByLang("en_GB", "citybuild.day.global", "§7%player% has changed the time to day");
        I18N.setDefaultByLang("en_GB", "citybuild.night.global", "§7%player% has changed the time to night");
        I18N.setDefaultByLang("en_GB", "citybuild.god.on", "§7You have put yourself in God mode");
        I18N.setDefaultByLang("en_GB", "citybuild.god.off", "§7You have put yourself out of God mode");
        I18N.setDefaultByLang("en_GB", "citybuild.god.on.other", "§7%player% has put you in god mode");
        I18N.setDefaultByLang("en_GB", "citybuild.god.on.other.notify", "§7You have set %player% to god mode.");
        I18N.setDefaultByLang("en_GB", "citybuild.god.off.other", "§7%player% has put you in god mode");
        I18N.setDefaultByLang("en_GB", "citybuild.god.off.other.notify", "§7You have set %player% to god mode.");
        I18N.setDefaultByLang("en_GB", "citybuild.god.usage", "§7Usage: /god Player");
        I18N.setDefaultByLang("en_GB", "citybuild.tp.self", "§7You have teleported to %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpo.self", "§7You have teleported to %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.tp.other", "§7%player% teleported you to you.");
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target.tp.notify", "§7You teleported %target1% to %target2%."); // Command Executor
        I18N.setDefaultByLang("en_GB", "citybuild.tpo.target.tpo.notify", "§7You teleported %target1% to %target2%."); // Command Executor
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target1", "§7%target2% was teleported to you by %player%."); // Player to you
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target2", "§7You have been teleported from %player% to %target1%."); // you to Player
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.self", "§7You have teleported to the spawn.");
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.other", "§7You have been teleported to the spawn by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.other.notify", "§7You %target% teleported to the spawn.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.request", "§7You can't send yourself a request!");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.accept", "§7%player% just accepted your request. You are going to teleport in 3 seconds.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.deny", "§7%player% has rejected your request.");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.expired", "§7This TPA isn't valid anymore.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.load", "§7Teleporting...");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.usage", "§7Usage: /tpa [Player]");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.message1", "§7%player% wants to teleport to you.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.message2", "§7You sent %player% a TPA request.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.deny", "§7You have denied the TPA request");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.accept", "§7You have accepted the TPA request");
        I18N.setDefaultByLang("en_GB", "citybuild.tpahere.self.accept", "§7%player% has accepted your request and will teleport to you in 3 seconds.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpahere.message1", "§7%player% sent you a teleport-request. Do you want to teleport?");
        I18N.setDefaultByLang("en_GB", "citybuild.tpahere.message2", "§7You have sent %player% a TPAHere request.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.yes", "§aAccept");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.no", "§cDeny");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.self", "§7You have set yourself to speed mode 1");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.self", "§7You have set yourself to speed mode 2");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.self", "§7You have set yourself to speed mode 3");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.self", "§7You have set yourself to speed mode 4");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.self", "§7You have set yourself to speed mode 5");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.self", "§7You have set yourself to speed mode 6");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.self", "§7You have set yourself to speed mode 7");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.self", "§7You have set yourself to speed mode 8");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.self", "§7You have set yourself to speed mode 9");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.self", "§7You have set yourself to speed mode 10");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.other", "§7You have been set to speed mode 1 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.other.notify", "§7You have set %player% to speed mode 1.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.other", "§7You have been set to speed mode 2 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.other.notify", "§7You have set %player% to speed mode 2.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.other", "§7You have been set to speed mode 3 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.other.notify", "§7You have set %player% to speed mode 3.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.other", "§7You have been set to speed mode 4 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.other.notify", "§7You have set %player% to speed mode 4.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.other", "§7You have been set to speed mode 5 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.other.notify", "§7You have set %player% to speed mode 5.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.other", "§7You have been set to speed mode 6 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.other.notify", "§7You have set %player% to speed mode 6.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.other", "§7You have been set to speed mode 7 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.other.notify", "§7You have set %player% to speed mode 7.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.other", "§7You have been set to speed mode 8 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.other.notify", "§7You have set %player% to speed mode 8.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.other", "§7You have been set to speed mode 9 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.other.notify", "§7You have set %player% to speed mode 9.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.other", "§7You have been set to speed mode 10 by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.other.notify", "§7You have set %player% to speed mode 10.");
        I18N.setDefaultByLang("en_GB", "citybuild.ectoggle", "§7This enderchest is locked");
        I18N.setDefaultByLang("en_GB", "citybuild.invtoggle", "§7This inventory is locked");
        I18N.setDefaultByLang("en_GB", "citybuild.enderchest.open", "§7Your enderchest is now open again.");
        I18N.setDefaultByLang("en_GB", "citybuild.enderchest.open", "§7Your enderchest is now closed.");
        I18N.setDefaultByLang("en_GB", "citybuild.inventar.close", "§7Your inventory is now closed.");
        I18N.setDefaultByLang("en_GB", "citybuild.inventar.open", "§7Your inventory is now open.");
        I18N.setDefaultByLang("en_GB", "citybuild.inventar.notallow", "§7You may not teleport to this player.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpoall", "§7You teleported everyone to you.");
        I18N.setDefaultByLang("en_GB", "citybuild.tp.koordi.self", "§7You teleported to the coordinates.");
        I18N.setDefaultByLang("en_GB", "citybuild.tp.koordi.other", "§7You have been teleported to certain coordinates.");
        I18N.setDefaultByLang("en_GB", "citybuild.repair.self", "§7The item in your hand has been repaired.");
        I18N.setDefaultByLang("en_GB", "citybuild.repair.other", "§7The item in your hand was repaired by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.repair.fail", "§7You cannot repair this item.");
        I18N.setDefaultByLang("en_GB", "citybuild.repair.other.notify", "§7You have repaired the item of %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.ec.usage", "§cBenutze: §7/ec <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.feed.usage", "§cBenutze: §7/feed <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.fly.usage", "§cBenutze: §7/fly <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.god.usage", "§cBenutze: §7/god <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.heal.usage", "§cBenutze: §7/heal <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.invsee.usage", "§cBenutze: §7/invsee <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.skull.usage", "§cBenutze: §7/skull [Player]");
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.usage", "§cBenutze: §7/spawn <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.usage", "§cBenutze: §7/speed [1-10] <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.teleport.usage", "§cBenutze: §7/teleport [Player] <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.tpo.usage", "§cBenutze: §7/tpo [Player] <Player>");
        I18N.setDefaultByLang("en_GB", "citybuild.weather.usage", "§cBenutze: §7/weather [clear|rain|thunder]");
        I18N.setDefaultByLang("en_GB", "citybuild.sign.success", "§7This item was successfully signed");
        I18N.setDefaultByLang("en_GB", "citybuild.sign.text", "§7Signed by §e%player% §7at §e%time%");
        I18N.setDefaultByLang("en_GB", "citybuild.repair.usage", "§cUsage: §7/repair [Player]");
        I18N.setDefaultByLang("en_GB", "citybuild.top.self", "§7You have been teleported to the Highest Block in the World");
        I18N.setDefaultByLang("en_GB", "citybuild.top.other", "§7You have been teleported to the highest block in the world by %player%.");
        I18N.setDefaultByLang("en_GB", "citybuild.top.other.notify", "§7You have teleported %player% to the highest block in the world.");
        I18N.setDefaultByLang("en_GB", "citybuild.top.usage", "§7Usage: /top [Player]");
        I18N.setDefaultByLang("en_GB", "citybuild.tptoggle.open", "§7From now on you can be teleported to again.");
        I18N.setDefaultByLang("en_GB", "citybuild.tptoggle.close", "§7From now on it is no longer possible to teleport to you.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpatoggle.close", "§7From now on you can no longer be sent a Tpa request.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpatoggle.open", "§7From now on you can be sent Tpa requests again.");
        I18N.setDefaultByLang("en_GB", "citybuild.tptoggle", "§7You may not teleport to this player.");
        I18N.setDefaultByLang("en_GB", "citybuild.tpatoggle", "§7You may not send a TPA to this player");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.notfound", "§7This tpa was not found");
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.usage", "§7Usage: /tpahere [Player]");

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
        Objects.requireNonNull(getCommand("repair")).setExecutor(new RepairCommand());
        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderChestCommand());
        Objects.requireNonNull(getCommand("invsee")).setExecutor(new InvseeCommand());
        Objects.requireNonNull(getCommand("speed")).setExecutor(new SpeedCommand());
        Objects.requireNonNull(getCommand("ectoggle")).setExecutor(new EcToggleCommand());
        Objects.requireNonNull(getCommand("invtoggle")).setExecutor(new InvToggleCommand());
        Objects.requireNonNull(getCommand("skull")).setExecutor(new SkullCommand());
        Objects.requireNonNull(getCommand("tpo")).setExecutor(new TpoCommand());
        Objects.requireNonNull(getCommand("tpoall")).setExecutor(new TpoAllCommand());
        Objects.requireNonNull(getCommand("sign")).setExecutor(new SignCommand());
        Objects.requireNonNull(getCommand("top")).setExecutor(new TopCommand());
        Objects.requireNonNull(getCommand("tpa")).setExecutor(new TPACommand());
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new TPAcceptCommand());
        Objects.requireNonNull(getCommand("tpdeny")).setExecutor(new TPACommand());
        Objects.requireNonNull(getCommand("tpahere")).setExecutor(new TPAHereCommand());

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
