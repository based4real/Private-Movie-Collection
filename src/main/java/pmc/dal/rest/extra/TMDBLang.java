package pmc.dal.rest.extra;

import java.util.Locale;

public enum TMDBLang {
    SYSTEM(Locale.getDefault().getLanguage()),
    DANISH("da"),
    ENGLISH("en"),
    DEUTSCH("de"),
    FRENCH("fr"),
    SWEDISH("se"),
    NORWEGIAN("no");

    private final String language;

    TMDBLang(String language) {
        this.language = language;
    }

    public String get() {
        return "&language=" + language;
    }
}
