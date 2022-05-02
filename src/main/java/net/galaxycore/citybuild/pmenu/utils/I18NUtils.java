package net.galaxycore.citybuild.pmenu.utils;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.*;

public class I18NUtils {
    public static final int MAX_LINES_PER_PAGE = 14;
    public static final int MAX_PAGES = 100;
    public static final int MAX_PIXELS_PER_LINE = 19*5;

    public static final Map<Character, Integer> CHARACTER_WIDTH = new HashMap<>();

    static {
        CHARACTER_WIDTH.put(' ',  3);
        CHARACTER_WIDTH.put('!',  1);
        CHARACTER_WIDTH.put('"',  3);
        CHARACTER_WIDTH.put('\'', 1);
        CHARACTER_WIDTH.put('(',  3);
        CHARACTER_WIDTH.put(')',  3);
        CHARACTER_WIDTH.put('*',  3);
        CHARACTER_WIDTH.put(',',  1);
        CHARACTER_WIDTH.put('.',  1);
        CHARACTER_WIDTH.put(':',  1);
        CHARACTER_WIDTH.put(';',  1);
        CHARACTER_WIDTH.put('<',  4);
        CHARACTER_WIDTH.put('>',  4);
        CHARACTER_WIDTH.put('@',  6);
        CHARACTER_WIDTH.put('I',  3);
        CHARACTER_WIDTH.put('[',  3);
        CHARACTER_WIDTH.put(']',  3);
        CHARACTER_WIDTH.put('`',  2);
        CHARACTER_WIDTH.put('f',  4);
        CHARACTER_WIDTH.put('i',  1);
        CHARACTER_WIDTH.put('k',  4);
        CHARACTER_WIDTH.put('l',  2);
        CHARACTER_WIDTH.put('t',  3);
        CHARACTER_WIDTH.put('{',  3);
        CHARACTER_WIDTH.put('|',  1);
        CHARACTER_WIDTH.put('}',  3);
        CHARACTER_WIDTH.put('~',  6);
    }

    public static int getCharacterWidth(char c) {
        return CHARACTER_WIDTH.getOrDefault(c, 5);
    }

    public static int getCharacterWidth(String s) {
        int width = 0;
        for (char c : s.toCharArray()) {
            width += getCharacterWidth(c);
        }
        return width;
    }

    public static List<Page> calcPages(String src) {
        String[] withForcedLineBreaks = src.split("\n");

        List<String> lines = Lists.newArrayList();

        for (String line : withForcedLineBreaks) {
            int len = getCharacterWidth(line);
            if (len > MAX_PIXELS_PER_LINE) {
                String[] words = line.split(" ");
                StringBuilder sb = new StringBuilder();
                for (String word : Arrays.stream(words).toList().subList(0, words.length - 1)) {
                    int wordLen = getCharacterWidth(word);
                    if (sb.length() + wordLen > MAX_PIXELS_PER_LINE) {
                        lines.add(sb.toString());
                        sb = new StringBuilder();
                    }
                    sb.append(word).append(" ");
                }

                String lastWord = words[words.length - 1];
                int lastWordLen = getCharacterWidth(lastWord);
                if (lastWordLen > MAX_PIXELS_PER_LINE) {
                    for (int i = 0; i < lastWord.length(); i++) {
                        String sub = lastWord.substring(0, i + 1);
                        int subLen = getCharacterWidth(sub);
                        if (subLen < MAX_PIXELS_PER_LINE) {
                            continue;
                        }
                        if (sb.length() > 0) {
                            lines.add(sb.toString());
                        }
                        sb = new StringBuilder();
                        lines.add(lastWord.substring(0, i));
                        lines.add(lastWord.substring(i));
                        break;
                    }
                } else if (getCharacterWidth(sb.toString()) + lastWordLen > MAX_PIXELS_PER_LINE) {
                    if (sb.length() > 0) {
                        lines.add(sb.toString());
                    }
                }

                if (sb.length() > 0) {
                    lines.add(sb.toString());
                }
            } else {
                lines.add(line);
            }
        }

        if(lines.size() <= MAX_LINES_PER_PAGE) {
            Page page = new Page();
            page.texts = lines;
            page.tocEntries = lines.stream().filter(s -> s.startsWith("#")).toArray(String[]::new);
            return List.of(page);
        }

        List<Page> pages = Lists.newArrayList();
        List<String> tocEntries = Lists.newArrayList();
        List<String> texts = Lists.newArrayList();
        int lineCount = 0;
        for (String line : lines) {
            if (lineCount > MAX_LINES_PER_PAGE) {
                Page page = new Page();
                page.texts = texts;
                page.tocEntries = tocEntries.toArray(new String[0]);
                pages.add(page);
                lineCount = 0;
                tocEntries = Lists.newArrayList();
                texts = Lists.newArrayList();
            }
            System.out.println(line);
            if (line.startsWith("#")) {
                tocEntries.add(line);
            }
            texts.add(line);
            lineCount++;
        }
        return pages;
    }

    public static Book getBook(String key, Player player, boolean toc) {
        String value = I18N.getS(player, key);
        String[] withForcedPageBreaks = value.split("\n---\n");
        List<Page> pages = Lists.newArrayList();
        for (String page : withForcedPageBreaks) {
            pages.addAll(calcPages(page));
        }
        
        List<List<Component>> componentsPre = pages.stream().map(page -> page.texts.stream().map(Component::text).toList()).toList().stream().map(textComponents -> textComponents.stream().map(textComponent -> (Component) textComponent).toList()).toList();;

        Book book = new Book();
        book.pages = new ArrayList<>();
        for (List<Component> components : componentsPre) {
            Optional<Component> optional = components.stream().reduce(Component::append);
            optional.ifPresent(component -> book.pages.add(component));
        }
        book.tocEntries = new HashMap<>();

        if(toc) {
            for(int i = 0; i < componentsPre.size(); i++) {
                List<Component> page = componentsPre.get(i);
                for(Component component : page) {
                    if(component instanceof TextComponent textComponent) {
                        if(textComponent.content().startsWith("#")) {
                            book.tocEntries.put(i, textComponent.content());
                        }
                    }
                }
            }
        }

        return book;
    }

    @Getter
    @Setter
    @ToString
    static class Page {
        private List<String> texts;
        private String[] tocEntries;
    }

    @Getter
    @Setter
    @ToString
    public static class Book {
        private List<Component> pages;
        private HashMap<Integer, String> tocEntries;
    }
}
