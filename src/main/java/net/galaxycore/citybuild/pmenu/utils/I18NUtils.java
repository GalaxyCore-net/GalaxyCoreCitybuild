package net.galaxycore.citybuild.pmenu.utils;

import lombok.Getter;
import lombok.Setter;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class I18NUtils {
    public static final int MAX_LINES_PER_PAGE = 14;
    public static final int MAX_PAGES = 100;
    public static final int MAX_PIXELS_PER_LINE = 57;

    public static final Map<Character, Integer> CHARACTER_WIDTH = new HashMap<>();

    static {
        CHARACTER_WIDTH.put(' ', 3);
        CHARACTER_WIDTH.put('!', 1);
        CHARACTER_WIDTH.put('"', 3);
        CHARACTER_WIDTH.put('\'', 1);
        CHARACTER_WIDTH.put('(', 3);
        CHARACTER_WIDTH.put(')', 3);
        CHARACTER_WIDTH.put('*', 3);
        CHARACTER_WIDTH.put(',', 1);
        CHARACTER_WIDTH.put('.', 1);
        CHARACTER_WIDTH.put(':', 1);
        CHARACTER_WIDTH.put(';', 1);
        CHARACTER_WIDTH.put('<', 4);
        CHARACTER_WIDTH.put('>', 4);
        CHARACTER_WIDTH.put('@', 6);
        CHARACTER_WIDTH.put('I', 3);
        CHARACTER_WIDTH.put('[', 3);
        CHARACTER_WIDTH.put(']', 3);
        CHARACTER_WIDTH.put('`', 2);
        CHARACTER_WIDTH.put('f', 4);
        CHARACTER_WIDTH.put('i', 1);
        CHARACTER_WIDTH.put('k', 4);
        CHARACTER_WIDTH.put('l', 2);
        CHARACTER_WIDTH.put('t', 3);
        CHARACTER_WIDTH.put('{', 3);
        CHARACTER_WIDTH.put('|', 1);
        CHARACTER_WIDTH.put('}', 3);
        CHARACTER_WIDTH.put('~', 6);
    }

    public static int getCharacterWidth(char c) {
        return CHARACTER_WIDTH.getOrDefault(c, 5);
    }

    private static Page calcPage(String src) {
        Page page = new Page();


        return null;
    }

    public static List<Component> getBookPages(String key, Player player, boolean toc) {
        String value = I18N.getS(player, key);
        String[] withForcedPageBreaks = value.split("\n---\n");
        return null;
    }


    @Getter
    @Setter
    static class Page {
        private String text;
        private String[] tocEntries;
    }
}
