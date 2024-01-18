package pmc.gui.utils;

import javafx.util.StringConverter;

public class TimeStringConverter extends StringConverter<Integer> {
    @Override
    public String toString(Integer object) {
        if (object == null) {
            return "";
        }

        int hours = object / 3600000;
        int minutes = (object % 3600000) / 60000;
        int seconds = (object % 60000) / 1000;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    @Override
    public Integer fromString(String string) {
        if (string == null || string.trim().isEmpty()) {
            return 0;
        }

        String[] parts = string.split(":");
        if (parts.length < 2 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid time format, must be mm:ss or hh:mm:ss");
        }

        int hours = 0;
        int minutes;
        int seconds;

        if (parts.length == 3) {
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
            seconds = Integer.parseInt(parts[2]);
        } else {
            minutes = Integer.parseInt(parts[0]);
            seconds = Integer.parseInt(parts[1]);
        }

        return (hours * 3600 + minutes * 60 + seconds) * 1000;
    }
}