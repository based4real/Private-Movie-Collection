package pmc.gui.utils;

import javafx.util.StringConverter;

public class TimeStringConverter extends StringConverter<Integer> {
    @Override
    public String toString(Integer object) {
        if (object == null) {
            return "";
        }

        // Convert milliseconds to mm:ss
        int minutes = object / 60000;
        int seconds = (object % 60000) / 1000;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public Integer fromString(String string) {
        if (string == null || string.trim().isEmpty()) {
            return 0;
        }

        // Split the string into minutes and seconds
        String[] parts = string.split(":");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid time format, must be mm:ss");
        }

        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return (minutes * 60 + seconds) * 1000;
    }
}