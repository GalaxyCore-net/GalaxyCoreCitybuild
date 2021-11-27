package net.galaxycore.citybuild.utils;

public class FastUtils {
    private static final int[] validDigitsForPosInt = new int[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static boolean isPositiveInt(String toTest) {
        // O(n)
        char[] toTestChars = toTest.toCharArray();
        for (char toTestChar : toTestChars) { if (!intArrayContains(validDigitsForPosInt, toTestChar)) return false; }

        return true;
    }

    public static boolean intArrayContains(int[] array, int object) {
        for (int i : array) { if (i == object) return true; }
        return false;
    }
}
