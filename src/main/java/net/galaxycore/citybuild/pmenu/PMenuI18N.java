package net.galaxycore.citybuild.pmenu;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.entity.Player;

public enum PMenuI18N {
    BACK("back", "§eZurück", "§eBack", false),
    NEXT("next", "§eNächste Seite", "§eNext Page", false),
    PREV("prev", "§eVorherige Seite", "§eLast Page", false),
    PAGE("page", "§eSeite ", "§ePage ", false),
    SEARCH("search", "§eSuchen", "§eSuchen", false),
    TITLE_MAIN("main.title", "§ePMenu", "§ePMenu", false),
    MAIN_WARPS("main.warps", "§aWarp", "§aWarp", false),
    MAIN_PLOTS("main.plots", "§aMeine Plots", "§aMy Plots", false),
    MAIN_THIS("main.thisplot", "§aDieses Plot", "§aThis Plots", false),
    MAIN_LICENCES("main.licences", "§aShop Lizenzen", "§aShop Licences", false),
    MAIN_PLOT_VISIT("main.plot.visit", "§aPlot besuchen", "§aPlot visit", false),
    MAIN_HELP("main.help", "§aHilfe", "§aHelp", false),
    MAIN_PLOT_BUY("main.plot.buy", "§aPlot kaufen", "Buy a Plot", false),
    TITLE_WARP("warp.title", "§eWarps", "§eWarps", false),
    WARP_SUCCESS("warp.success", "§7Du wurdest teleportiert", "§7You were teleported", true),
    TITLE_PLOTS("plots.title", "§eMeine Plots", "§eMy Plots", false),
    NOT_ON_A_PLOT("err.notonaplot", "§cDu befindest dich nicht auf einem Plot!", "§cYou are not on a plot!", true),
    PLOT_NOT_CLAIMED("err.plotnotclaimed", "§cDieses Plot gehört niemandem. Beanspruche es mit §e/p claim§c!", "§cThis plot is not owned by anyone. Claim it using §e/p claim§c!", true),
    NO_PLOT_PERMISSIONS("err.pnoperms", "§cDu hast keine Rechte, dieses Plot zu bearbeiten!", "§cYou do not have enough permissions to edit this plot!", true),
    TITLE_PLOTINFO("plotinfo.title", "§ePlotinfo", "§ePlotinfo", false),
    PLOTINFO_FLAGS("plotinfo.flags", "§eEigenschaften", "§eFlags", false),
    PLOTINFO_FLAGS_NONE("plotinfo.flags.none", "§7Keine", "§7None", false),
    PLOTINFO_SOMEONES_PLOT("plotinfo.someonesplot", "§e%p%'s Plot", "§e%p%'s Plot", false),
    PLOTINFO_SOMEONES_PLOT_ID("plotinfo.someonesplot.id", "§eID: ", "§eID: ", false),
    PLOTINFO_SOMEONES_PLOT_ALIAS("plotinfo.someonesplot.alias", "§eAlias: ", "§eAlias: ", false),
    PLOTINFO_SOMEONES_PLOT_ALIAS_NONE("plotinfo.someonesplot.alias.none", "Keine", "None", false),
    PLOTINFO_SOMEONES_PLOT_BIOME("plotinfo.someonesplot.biome", "§eBiom: ", "§eBiome: ", false),
    PLOTINFO_PEOPLE("plotinfo.people", "§ePersonen", "§ePeople", false),
    PLOTINFO_PEOPLE_TRUSTED("plotinfo.people.trusted", "§eVertraut:", "§eTrusted:", false),
    PLOTINFO_PEOPLE_MEMBER("plotinfo.people.member", "§eMitglieder:", "§eMembers:", false),
    PLOTINFO_PEOPLE_BANNED("plotinfo.people.banned", "§eVerboten:", "§eBanned:", false),
    PLOTINFO_PEOPLE_NONE("plotinfo.people.none", "Keine", "None", false),
    FLAGS_TITLE("flags.edit.title", "§eEigenschaften bearbeiten", "§eModify Flags", false),
    FLAGS_CURRENTVAL("flags.currentval", "§eAktueller Wert: ", "§eCurrent Value: ", false),
    FLAGS_EDIT("flags.edit", "§eEigenschaft Editieren", "§eEdit Flag", false),
    FLAGS_ACTIVATE("flags.activate", "§aAktivieren", "§aActivate", false),
    FLAGS_DEACTIVATE("flags.deactivate", "§cDeaktivieren", "§cDeactivate", false),
    FLAGS_CHANGE("flags.change", "§cÄndern", "§cChange", false),
    FLAGS_SUN("flags.sun", "§cSonne", "§cSun", false),
    FLAGS_RAIN("flags.rain", "§cRegen", "§cRain", false),
    FLAGS_TIP_ADD("flags.tip.add", "§7Hinweis: Klicke auf einen Block um ihn zur Liste hinzuzufügen oder zu entfernen", "§7Notice: Click on a block to add or remove it from the final list", false),
    FLAGS_NFE("flags.nfe", "§cDas ist keine Zahl", "§cThat's not a number", false),
    PLAYERNOTFOUND("player404", "§cDieser Spieler wurde nicht gefunden", "§cThis player was not found", true),
    RESET("reset", "§cZurücksetzen", "§cReset", false),
    TELEPORTED("tp", "§7Du wurdest teleportiert.", "§7You were teleported", true),
    NOT_ON_PLOT("not_on_plot", "§cDu befindest dich nicht auf einem Grundstück", "§cYou're not on a plot", false),
    NOT_YOUR_PLOT("not_your_plot", "§cDies ist nicht dein Grundstück", "§cThis is not your plot", false),
    FETCHING_PLAYERS_TIMEOUT("fetching_players_timeout", "§cFehler beim Herunterladen der Spielerdaten", "§cError while fetching player data", false);

    private final String key;

    PMenuI18N(String key, String de, String en, boolean prefix) {
        this.key = "citybuild.pmenu." + key;
        I18N.setDefaultByLang("de_DE", this.key, de, prefix);
        I18N.setDefaultByLang("en_GB", this.key, en, prefix);
    }

    public String get(Player player) {
        return I18N.getByPlayer(player, key);
    }

    public void __call__() {
        /*I18N Registrator Call*/
    }

    public void send(Player player) {
        player.sendMessage(this.get(player));
    }
}
