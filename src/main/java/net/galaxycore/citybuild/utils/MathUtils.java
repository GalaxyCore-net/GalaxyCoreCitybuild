package net.galaxycore.citybuild.utils;

public class MathUtils {

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

}
