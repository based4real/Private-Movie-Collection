package pmc.gui.utils;

public class StringHandler {

    public static String trimName(String name, int length) {
        return name.length() > length ?
                name.substring(0, length) + ".." :
                name;

    }
}
