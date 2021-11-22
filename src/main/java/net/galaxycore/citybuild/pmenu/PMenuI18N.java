package net.galaxycore.citybuild.pmenu;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import org.bukkit.entity.Player;

public enum PMenuI18N {
    TITLE_MAIN("main.title", "§ePMenu", "§ePMenu");

    private final String key;

    PMenuI18N(String key, String de, String en) {
        this.key =  "citybuild.pmenu." + key;
        I18N.setDefaultByLang("de_DE", this.key, de);
        I18N.setDefaultByLang("en_GB", this.key, en);
    }

    public String get(Player player) {
        return I18N.getByPlayer(player, key);
    }

    public void __call__() {
        /*I18N Registrator Call*/
    }
}
