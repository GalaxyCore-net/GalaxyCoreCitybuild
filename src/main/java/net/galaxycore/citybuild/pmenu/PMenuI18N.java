package net.galaxycore.citybuild.pmenu;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.entity.Player;

public enum PMenuI18N {
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
    TITLE_PLOTINFO("plotinfo.title", "§ePlotinfo", "§ePlotinfo", false),
    PLOTINFO_FLAGS("plotinfo.flags", "§eFlags", "§eFlags", false),
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
    ;

    private final String key;

    PMenuI18N(String key, String de, String en, boolean prefix){
        this.key =  "citybuild.pmenu." + key;
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
