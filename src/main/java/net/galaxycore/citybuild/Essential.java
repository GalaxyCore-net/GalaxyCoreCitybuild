package net.galaxycore.citybuild;

import lombok.SneakyThrows;
import net.galaxycore.citybuild.commands.*;
import net.galaxycore.citybuild.listeners.*;
import net.galaxycore.citybuild.pmenu.PMenuDistributor;
import net.galaxycore.citybuild.scoreboard.CustomScoreBoardManager;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.scoreboards.ScoreBoardController;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Essential extends JavaPlugin {
    private static Essential instance;
    private static GalaxyCoreCore core;
    private ConfigNamespace configNamespace;
    private PMenuDistributor pMenuDistributor;

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

        pMenuDistributor = new PMenuDistributor();

        configNamespace = core.getDatabaseConfiguration().getNamespace("galaxycity");
        configNamespace.setDefault("spawn.world", "world");
        configNamespace.setDefault("spawn.x", "186");
        configNamespace.setDefault("spawn.y", "65");
        configNamespace.setDefault("spawn.z", "-278");
        configNamespace.setDefault("spawn.yaw", "-90");
        configNamespace.setDefault("spawn.pitch", "0");
        configNamespace.setDefault("plotwelt.plot_width", "50");
        configNamespace.setDefault("plotwelt.road_width", "7");
        configNamespace.setDefault("plotwelt.plot_tp_offset", "3|1|3|-45|0");
        configNamespace.setDefault("max_player_plots", "5");

        I18N.setDefaultByLang("de_DE", "citybuild.noperms", "§7Du hast keine Berechtigung für diesen Command.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.noplayerfound", "§7Dieser Spieler ist nicht online", true);
        I18N.setDefaultByLang("de_DE", "citybuild.creative", "§7Du bist nun im Kreativmodus", true);
        I18N.setDefaultByLang("de_DE", "citybuild.creative.set", "§7Du hast %player% in den Kreativmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.survival", "§7Du bist nun im Survivalmodus", true);
        I18N.setDefaultByLang("de_DE", "citybuild.survival.set", "§7Du hast %player% in den Suvivalmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.spectator", "§7Du bist nun im Spectatormodus", true);
        I18N.setDefaultByLang("de_DE", "citybuild.spectator.set", "§7Du hast %player% in den Spectatormodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.adventure", "§7Du bist nun im Adventuremodus", true);
        I18N.setDefaultByLang("de_DE", "citybuild.adventure.set", "§7Du hast %player% in den Adventuremodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.gamemode.usage", "§7Benutze: /gamemode <0|1|2|3>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.fly.on", "§7Du wurdest in den Flugmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.fly.off", "§7Du wurdest aus dem Flugmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.fly.on.other", "§7Du wurdest von %player% in den Flugmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.fly.off.other", "§7Du wurdest von %player% aus dem Flugmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.fly.set.on", "§7Du hast %player% in den Flugmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.fly.set.off", "§7Du hast %player% aus dem Flugmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.feed", "§7Deine Hungerleiste wurde aufgefüllt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.feed.other", "§7Deine Hungerleiste wurde von %player% aufgefüllt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.feed.other.notify", "§7Du hast %player%´s Hungerleiste aufgefüllt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.heal.self", "§7Du hast dich geheilt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.heal.other", "§7Du hast %player% geheilt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.heal.other.self", "§7Du wurdest von %player% geheilt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.debug", "§7Sollte jetzt eigentlich funktionieren %player%", true);
        I18N.setDefaultByLang("de_DE", "citybuild.time.day", "§7Du hast die Zeit auf Tag gesetzt ", true);
        I18N.setDefaultByLang("de_DE", "citybuild.time.night", "§7Du hast die Zeit auf Nacht gesetzt ", true);
        I18N.setDefaultByLang("de_DE", "citybuild.weather.clear", "§7Du hast das Wetter auf Klar geändert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.weather.rain", "§7Du hast das Wetter auf Regen geändert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.weather.thunder", "§7Du hast das Wetter auf Gewitter geändert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.cooldown", "§7Du musst noch bis %time% warten", true);
        I18N.setDefaultByLang("de_DE", "citybuild.hat", "§7Viel Spaß mit deinem neuen Hut!", true);
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.clear", "§7%player% hat das Wetter auf Klar geändert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.rain", "§7%player% hat das Wetter auf Regen geändert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.weather.global.thunder", "§7%player% hat das Wetter auf Gewitter geändert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.day.global", "§7%player% hat die Zeit auf Tag verändert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.night.global", "§7%player% hat die Zeit auf Nacht verändert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.god.on", "§7Du hast dich in den Gottmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.god.off", "§7Du hast dich aus dem Gottmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.god.on.other", "§7%player% hat dich in den Gottmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.god.on.other.notify", "§7Du hast %player% in den Gottmodus", true);
        I18N.setDefaultByLang("de_DE", "citybuild.god.off.other", "§7%player% hat dich in den Gottmodus gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.god.off.other.notify", "§7Du hast %player% in den Gottmodus", true);
        I18N.setDefaultByLang("de_DE", "citybuild.god.usage", "§7Benutze: /god Player", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tptoggle", "§7Du darfst dich nicht zu diesem Spieler teleportieren.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpo.self", "§7Du hast dich zu %player% teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpo.target.tpo.notify", "§7Du hast %target1% zu %target2% teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tp.self", "§7Du hast dich zu %player% teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tp.other", "§7%player% hat dich zu dir teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target.tp.notify", "§7Du hast %target1% zu %target2% teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target1", "§7%target1% wurde von %player% zu dir teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tp.target2", "§7Du wurdest von %player% zu %target2% teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.self", "§7Du hast dich zum Spawn teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.other", "§7Du wurdest von %player% zum Spawn teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.other.notify", "§7Du hast %player% zum Spawn teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.request", "§7Du kannst dir nicht selber eine Anfrage schicken!", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.accept", "§7%player% hat deine Anfrage angenommen und du wirst in 3 Sekunden teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.self.deny", "§7%player% hat deine Anfrage abgelehnt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.notfound", "§7Diese Tpa wurde nicht gefunden", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.expired", "§7Diese Tpa ist nicht mehr §cgültig", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.load", "§7Teleportiere ...", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.deny", "§7Du hast die TPA-Anfrage abgelehnt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.accept", "§7Du hast die TPA-Anfrage angenommen", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.usage", "§7Benutze: /tpa [Player]", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.message1", "§7%player% möchte sich zu dir teleportieren.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.message2", "§7Du hast %player% eine TPA Anfrage gesendet.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.self.accept", "§7%player% hat deine Anfrage angenommen und wird in 3 Sekunden zu dir teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.message1", "§7%player% hat dir eine Teleport-Anfrage gesendet. Möchtest du dich teleportieren?", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.message2", "§7Du hast %player% eine TPAHere Anfrage gesendet.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.yes", "§aAkzeptieren");
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.no", "§cAblehnen");
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.self", "§7Du hast dich in den Speedmodus 1 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.self", "§7Du hast dich in den Speedmodus 2 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.self", "§7Du hast dich in den Speedmodus 3 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.self", "§7Du hast dich in den Speedmodus 4 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.self", "§7Du hast dich in den Speedmodus 5 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.self", "§7Du hast dich in den Speedmodus 6 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.self", "§7Du hast dich in den Speedmodus 7 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.self", "§7Du hast dich in den Speedmodus 8 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.self", "§7Du hast dich in den Speedmodus 9 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.self", "§7Du hast dich in den Speedmodus 10 gesetzt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.other", "§7Du wurdest von %player% in den Speedmodus 1 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.1.other.notify", "§7Du hast %player% in den Speedmodus 1 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.other", "§7Du wurdest von %player% in den Speedmodus 2 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.2.other.notify", "§7Du hast %player% in den Speedmodus 2 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.other", "§7Du wurdest von %player% in den Speedmodus 3 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.3.other.notify", "§7Du hast %player% in den Speedmodus 3 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.other", "§7Du wurdest von %player% in den Speedmodus 4 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.4.other.notify", "§7Du hast %player% in den Speedmodus 4 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.other", "§7Du wurdest von %player% in den Speedmodus 5 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.5.other.notify", "§7Du hast %player% in den Speedmodus 5 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.other", "§7Du wurdest von %player% in den Speedmodus 6 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.6.other.notify", "§7Du hast %player% in den Speedmodus 6 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.other", "§7Du wurdest von %player% in den Speedmodus 7 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.7.other.notify", "§7Du hast %player% in den Speedmodus 7 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.other", "§7Du wurdest von %player% in den Speedmodus 8 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.8.other.notify", "§7Du hast %player% in den Speedmodus 8 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.other", "§7Du wurdest von %player% in den Speedmodus 9 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.9.other.notify", "§7Du hast %player% in den Speedmodus 9 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.other", "§7Du wurdest von %player% in den Speedmodus 10 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.10.other.notify", "§7Du hast %player% in den Speedmodus 10 gesetzt.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.ectoggle", "§7Diese Enderchest ist gesperrt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.invtoggle", "§7Dieses Inventar ist gesperrt", true);
        I18N.setDefaultByLang("de_DE", "citybuild.enderchest.open", "§7Deine Enderchest ist nun wieder offen.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.enderchest.open", "§7Deine Enderchest ist nun geschlossen.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.inventar.close", "§7Dein Inventar ist nun geschlossen.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.inventar.open", "§7Dein Inventar ist nun offen.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.inventar.notallow", "§7Du darfst dich nicht zu diesem Spieler teleportieren.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpoall", "§7Du hast alle zu dir Teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.ec.usage", "§7Benutze: §e/ec <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.feed.usage", "§7Benutze: §e/feed <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.fly.usage", "§7Benutze: §e/fly <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.god.usage", "§7Benutze: §e/god <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.heal.usage", "§7Benutze: §e/heal <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.invsee.usage", "§7Benutze: §e/invsee <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.skull.usage", "§7Benutze: §e/skull [Spieler]", true);
        I18N.setDefaultByLang("de_DE", "citybuild.spawn.usage", "§7Benutze: §e/spawn <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.repair.usage", "§7Benutze: §e/spawn <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.speed.usage", "§7Benutze: §e/speed [1-10] <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.teleport.usage", "§7Benutze: §e/teleport [Spieler] <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpo.usage", "§7Benutze: §e/tpo [Spieler] <Spieler>", true);
        I18N.setDefaultByLang("de_DE", "citybuild.weather.usage", "§7Benutze: §e/weather [clear|rain|thunder]", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tp.koordi.self", "§7Du hast dich zu den Koordinaten Teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tp.koordi.other", "§7Du wurdest zu bestimmten Koordinaten Teleportiert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.repair.self", "§7Das Item in deiner Hand wurde repariert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.repair.other", "§7Das Item in deiner Hand wurde von %player% repariert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.repair.other.notify", "§7Du hast das Item von %player% repariert.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.repair.fail", "§7Du kannst dieses Item nicht reparieren.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.sign.success", "§7Dieses Item wurde erfolgreich signiert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.sign.text", "§7Signiert von §e%player% §7am §e%time%");
        I18N.setDefaultByLang("de_DE", "citybuild.top.self", "§7Du wurdest auf den höchsten Block der Welt Teleportiert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.top.other", "§7Du wurdest von %player% auf den höchsten Block der Welt Teleportiert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.top.other.notify", "§7Du hast %player% auf den höchsten Block der Welt Teleportiert", true);
        I18N.setDefaultByLang("de_DE", "citybuild.top.usage", "§7Benutze: /top [Spieler]", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpatoggle", "§7Du darfst diesem Spieler keine TPA senden", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tptoggle.open", "§7Ab jetzt kann man sich wieder zu dir teleportieren.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tptoggle.close", "§7Ab jetzt kann man sich nicht mehr zu dir teleportieren.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpatoggle.close", "§7Ab jetzt kann man dir keine Tpa-Anfrage mehr senden.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpatoggle.open", "§7Ab jetzt kann man dir wieder Tpa-Anfragen senden.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpahere.usage", "§7Benutze: /tpahere [Player]", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpaall", "§7Du hast jedem eine TPA gesendet", true);
        I18N.setDefaultByLang("de_DE", "citybuild.score.rank", "§eRang:");
        I18N.setDefaultByLang("de_DE", "citybuild.score.onlinetime", "§eSpielzeit:");
        I18N.setDefaultByLang("de_DE", "citybuild.score.server", "§eServer:");
        I18N.setDefaultByLang("de_DE", "citybuild.score.coins", "§eCoins:");
        I18N.setDefaultByLang("de_DE", "citybuild.score.teamspeak", "§eTeamspeak:");
        I18N.setDefaultByLang("de_DE", "citybuild.setwarp.usage", "§cBenutze: §e/setwarp [Name] [Pos] §cund halte ein Item in der Hand", true);
        I18N.setDefaultByLang("de_DE", "citybuild.delwarp.usage", "§cBenutze: §e/delwarp [Pos]", true);
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.plots.error_title", "§cBeim Anfragen der Grundstücke ist ein Fehler aufgetreten");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.plots.error_lore", "§cBitte melde diesen Fehler bei einem Teammitglied");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.plots.no_plots_title", "§cDu hast bist jetzt noch keine Grundstücke");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.plots.no_plots_lore", "§cHol' dir eins mit /p claim");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.claiming", "§6Holen");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.teleport", "§6Teleportieren");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.settings", "§6Einstellungen");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.chat", "§6Chat");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.schematic", "§6Schematics");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.appereance", "§6Aussehen");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.info", "§6Info");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.debug", "§6Debug");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.administration", "§6Administration");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.help.title", "§6Hilfe");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.claim.not_on_plot", "§cDu befindest dich nicht auf einem Grundstück");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.claim.plot_already_claimed", "§CDieses Grundstück ist bereits besetzt");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.claim.plot_limit_exceeded", "§cDu bist am Grundstückslimit angekommen. Daher kannst du keine weiteren Grundstücke besitzen.");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.claim.claim_title", "§aHole dir dieses Grundstück");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.claim.claim_lore", "§aDies wird dein %plot% Grundstück");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.claim.cancel", "§cAbbrechen");
        I18N.setDefaultByLang("de_DE", "citybuild.pmenu.claim.title", "§6Grundstück holen");

        I18N.setDefaultByLang("en_GB", "citybuild.noperms", "§7You're not permitted to use this", true);
        I18N.setDefaultByLang("en_GB", "citybuild.noplayerfound", "§7This Player isn't online", true);
        I18N.setDefaultByLang("en_GB", "citybuild.creative", "§7Your Gamemode has been set to Creative Mode ", true);
        I18N.setDefaultByLang("en_GB", "citybuild.creative.set", "§7You have set %player% to Creative Mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.survival", "§7Your Gamemode has been set to Survival Mode ", true);
        I18N.setDefaultByLang("en_GB", "citybuild.survival.set", "§7You have set %player% to Survival Mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.spectator", "§7Your Gamemode has been set to Spectator Mode ", true);
        I18N.setDefaultByLang("en_GB", "citybuild.spectator.set", "§7You have set %player% to Spectator Mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.adventure", "§7Your Gamemode has been set to Adventure Mode ", true);
        I18N.setDefaultByLang("en_GB", "citybuild.adventure.set", "§7You have set %player% to Adventure Mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.gamemode.usage", "§7Usage: /gamemode <0|1|2|3>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.fly.on", "§7You have been set to flight mode.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.fly.off", "§7You have been put out of flight mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.fly.on.other", "§7You have been put into flight mode by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.fly.off.other", "§7You have been put out of flight mode by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.fly.set.on", "§7You have set %player% to flight mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.fly.set.off", "§7You have set %player% out of flight mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.feed", "§7Your hunger bar has been filled up", true);
        I18N.setDefaultByLang("en_GB", "citybuild.feed.other", "§7Your hunger bar has been filled up by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.feed.other.notify", "§7You have filled up %player%'s hunger bar", true);
        I18N.setDefaultByLang("en_GB", "citybuild.heal.self", "§7You have healed yourself", true);
        I18N.setDefaultByLang("en_GB", "citybuild.heal.other", "§7You have healed %player", true);
        I18N.setDefaultByLang("en_GB", "citybuild.heal.other.self", "§7You have been healed by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.debug", "§7Should actually work now %player%", true);
        I18N.setDefaultByLang("en_GB", "citybuild.time.day", "§7You have set the time to day ", true);
        I18N.setDefaultByLang("en_GB", "citybuild.time.night", "§7You have set the time to night ", true);
        I18N.setDefaultByLang("en_GB", "citybuild.weather.clear", "§7You changed the weather to clear", true);
        I18N.setDefaultByLang("en_GB", "citybuild.weather.rain", "§7You have changed the weather to rain", true);
        I18N.setDefaultByLang("en_GB", "citybuild.weather.thunder", "§7You have changed the weather to thunderstorms", true);
        I18N.setDefaultByLang("en_GB", "citybuild.cooldown", "§7You still have to wait until %time%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.hat", "§7Have fun with your new Hat!", true);
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.clear", "§7%player% changed the weather to clear", true);
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.rain", "§7%player% changed the weather to rain", true);
        I18N.setDefaultByLang("en_GB", "citybuild.weather.global.thunder", "§7%player% changed the weather to thunder", true);
        I18N.setDefaultByLang("en_GB", "citybuild.day.global", "§7%player% has changed the time to day", true);
        I18N.setDefaultByLang("en_GB", "citybuild.night.global", "§7%player% has changed the time to night", true);
        I18N.setDefaultByLang("en_GB", "citybuild.god.on", "§7You have put yourself in God mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.god.off", "§7You have put yourself out of God mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.god.on.other", "§7%player% has put you in god mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.god.on.other.notify", "§7You have set %player% to god mode.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.god.off.other", "§7%player% has put you in god mode", true);
        I18N.setDefaultByLang("en_GB", "citybuild.god.off.other.notify", "§7You have set %player% to god mode.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.god.usage", "§7Usage: /god Player", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tp.self", "§7You have teleported to %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpo.self", "§7You have teleported to %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpo.target.tpo.notify", "§7You teleported %target1% to %target2%.", true); // Command Executor
        I18N.setDefaultByLang("en_GB", "citybuild.tp.self", "§7You have teleported to %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tp.other", "§7%player% teleported to you.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target.tp.notify", "§7You teleported %target1% to %target2%.", true); // Command Executor
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target1", "§7%target1% was teleported to you by %player%.", true); // Player to you
        I18N.setDefaultByLang("en_GB", "citybuild.tp.target2", "§7You have been teleported from %player% to %target1%.", true); // you to Player
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.self", "§7You have teleported to the spawn.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.other", "§7You have been teleported to the spawn by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.other.notify", "§7You %target% teleported to the spawn.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.request", "§7You can't send yourself a request!", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.accept", "§7%player% just accepted your request. You are going to teleport in 3 seconds.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.self.deny", "§7%player% has rejected your request.", true);
        I18N.setDefaultByLang("de_DE", "citybuild.tpa.expired", "§7This TPA isn't valid anymore.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.load", "§7Teleporting...", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.usage", "§7Usage: /tpa [Player]", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.message1", "§7%player% wants to teleport to you.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.message2", "§7You sent %player% a TPA request.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.deny", "§7You have denied the TPA request", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.accept", "§7You have accepted the TPA request", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpahere.self.accept", "§7%player% has accepted your request and will teleport to you in 3 seconds.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpahere.message1", "§7%player% sent you a teleport-request. Do you want to teleport?", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpahere.message2", "§7You have sent %player% a TPAHere request.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.yes", "§aAccept");
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.no", "§cDeny");
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.self", "§7You have set yourself to speed mode 1", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.self", "§7You have set yourself to speed mode 2", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.self", "§7You have set yourself to speed mode 3", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.self", "§7You have set yourself to speed mode 4", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.self", "§7You have set yourself to speed mode 5", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.self", "§7You have set yourself to speed mode 6", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.self", "§7You have set yourself to speed mode 7", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.self", "§7You have set yourself to speed mode 8", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.self", "§7You have set yourself to speed mode 9", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.self", "§7You have set yourself to speed mode 10", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.other", "§7You have been set to speed mode 1 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.1.other.notify", "§7You have set %player% to speed mode 1.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.other", "§7You have been set to speed mode 2 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.2.other.notify", "§7You have set %player% to speed mode 2.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.other", "§7You have been set to speed mode 3 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.3.other.notify", "§7You have set %player% to speed mode 3.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.other", "§7You have been set to speed mode 4 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.4.other.notify", "§7You have set %player% to speed mode 4.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.other", "§7You have been set to speed mode 5 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.5.other.notify", "§7You have set %player% to speed mode 5.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.other", "§7You have been set to speed mode 6 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.6.other.notify", "§7You have set %player% to speed mode 6.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.other", "§7You have been set to speed mode 7 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.7.other.notify", "§7You have set %player% to speed mode 7.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.other", "§7You have been set to speed mode 8 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.8.other.notify", "§7You have set %player% to speed mode 8.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.other", "§7You have been set to speed mode 9 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.9.other.notify", "§7You have set %player% to speed mode 9.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.other", "§7You have been set to speed mode 10 by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.10.other.notify", "§7You have set %player% to speed mode 10.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.ectoggle", "§7This enderchest is locked", true);
        I18N.setDefaultByLang("en_GB", "citybuild.invtoggle", "§7This inventory is locked", true);
        I18N.setDefaultByLang("en_GB", "citybuild.enderchest.open", "§7Your enderchest is now open again.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.enderchest.open", "§7Your enderchest is now closed.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.inventar.close", "§7Your inventory is now closed.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.inventar.open", "§7Your inventory is now open.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.inventar.notallow", "§7You may not teleport to this player.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpoall", "§7You teleported everyone to you.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tp.koordi.self", "§7You teleported to the coordinates.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tp.koordi.other", "§7You have been teleported to certain coordinates.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.repair.self", "§7The item in your hand has been repaired.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.repair.other", "§7The item in your hand was repaired by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.repair.fail", "§7You cannot repair this item.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.repair.other.notify", "§7You have repaired the item of %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.ec.usage", "§cBenutze: §7/ec <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.feed.usage", "§cBenutze: §7/feed <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.fly.usage", "§cBenutze: §7/fly <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.god.usage", "§cBenutze: §7/god <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.heal.usage", "§cBenutze: §7/heal <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.invsee.usage", "§cBenutze: §7/invsee <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.skull.usage", "§cBenutze: §7/skull [Player]", true);
        I18N.setDefaultByLang("en_GB", "citybuild.spawn.usage", "§cBenutze: §7/spawn <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.speed.usage", "§cBenutze: §7/speed [1-10] <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.teleport.usage", "§cBenutze: §7/teleport [Player] <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpo.usage", "§cBenutze: §7/tpo [Player] <Player>", true);
        I18N.setDefaultByLang("en_GB", "citybuild.weather.usage", "§cBenutze: §7/weather [clear|rain|thunder]", true);
        I18N.setDefaultByLang("en_GB", "citybuild.sign.success", "§7This item was successfully signed", true);
        I18N.setDefaultByLang("en_GB", "citybuild.sign.text", "§7Signed by §e%player% §7at §e%time%");
        I18N.setDefaultByLang("en_GB", "citybuild.repair.usage", "§cUsage: §7/repair [Player]", true);
        I18N.setDefaultByLang("en_GB", "citybuild.top.self", "§7You have been teleported to the Highest Block in the World", true);
        I18N.setDefaultByLang("en_GB", "citybuild.top.other", "§7You have been teleported to the highest block in the world by %player%.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.top.other.notify", "§7You have teleported %player% to the highest block in the world.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.top.usage", "§7Usage: /top [Player]", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tptoggle.open", "§7From now on you can be teleported to again.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tptoggle.close", "§7From now on it is no longer possible to teleport to you.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpatoggle.close", "§7From now on you can no longer be sent a Tpa request.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpatoggle.open", "§7From now on you can be sent Tpa requests again.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tptoggle", "§7You may not teleport to this player.", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpatoggle", "§7You may not send a TPA to this player", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpa.notfound", "§7This tpa was not found", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpahere.usage", "§7Usage: /tpahere [Player]", true);
        I18N.setDefaultByLang("en_GB", "citybuild.tpaall", "§7You sent everyone a TPA", true);
        I18N.setDefaultByLang("en_GB", "citybuild.score.rank", "§eRank:");
        I18N.setDefaultByLang("en_GB", "citybuild.score.onlinetime", "§ePlaytime:");
        I18N.setDefaultByLang("en_GB", "citybuild.score.server", "§eServer:");
        I18N.setDefaultByLang("en_GB", "citybuild.score.coins", "§eCoins:");
        I18N.setDefaultByLang("en_GB", "citybuild.score.teamspeak", "§eTeamspeak:");
        I18N.setDefaultByLang("en_GB", "citybuild.setwarp.usage", "§cPlease use: §e/setwarp [Name] [Pos] §cand hold an item in your hand", true);
        I18N.setDefaultByLang("en_GB", "citybuild.delwarp.usage", "§cPlease use: §e/delwarp [Pos] ", true);
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.plots.error_title", "§cAn error occured while getting the plots");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.plots.error_lore", "§cPlease report this error to a staff member");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.plots.no_plots_title", "§cYou don't have any plots yet");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.plots.no_plots_lore", "§cClaim one using /p claim");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.claiming", "§6claiming");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.teleport", "§6teleport");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.settings", "§6settings");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.chat", "§6chat");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.schematic", "§6schematic");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.appereance", "§6appereance");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.info", "§6info");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.debug", "§6debug");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.administration", "§6administration");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.title", "§6Help");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.help.help", "§eHelp");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.claim.not_on_plot", "§cYou're not on a plot");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.claim.plot_already_claimed", "§CThis plot is already claimed");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.claim.plot_limit_exceeded", "§cYou exceeded the plot limit. Therefore, you can't claim more plots");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.claim.claim_title", "§aClaim this plot");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.claim.claim_lore", "§aThis will be your %plot% plot");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.claim.cancel", "§cCancel");
        I18N.setDefaultByLang("en_GB", "citybuild.pmenu.claim.title", "§6Claim");

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
        Objects.requireNonNull(getCommand("tpatoggle")).setExecutor(new TpaToggleCommand());
        Objects.requireNonNull(getCommand("tptoggle")).setExecutor(new TpToggleCommand());
        Objects.requireNonNull(getCommand("tpaall")).setExecutor(new TPAAllCommand());
        Objects.requireNonNull(getCommand("setwarp")).setExecutor(new SetWarpCommand());
        Objects.requireNonNull(getCommand("delwarp")).setExecutor(new DelWarpCommand());

        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(getConfigNamespace()), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);

        ScoreBoardController.setScoreBoardCallback(new CustomScoreBoardManager());

        PMenuDistributor.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public PMenuDistributor getPMenuDistributor() {
        return pMenuDistributor;
    }

    public void setpMenuDistributor(PMenuDistributor pMenuDistributor) {
        this.pMenuDistributor = pMenuDistributor;
    }
}
