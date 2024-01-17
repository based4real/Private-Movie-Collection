package pmc.gui.utils;

public class StringHandler {

    public static String trimName(String name, int length) {
        return name.length() > length ?
                name.substring(0, length) + ".." :
                name;
    }

    public static String getFirst(String txt) {
        return txt.substring(0, 1);
    }

    public static String getFirstAfterSpace(String txt) {
        if (txt == null || !txt.contains(" "))
            return "";

        int spaceIdx = txt.indexOf(" ");
        return txt.substring(spaceIdx + 1, spaceIdx + 2);
    }
}
