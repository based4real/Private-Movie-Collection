package pmc.bll.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigSystem {

    private static ConfigSystem instance;
    private static final String CONFIG_FILE = "config/config.settings";

    public static ConfigSystem getInstance() {
        if (instance == null)
            instance = new ConfigSystem();

        return instance;
    }

    private static Properties getConfigProperties() throws IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream((CONFIG_FILE)));
        return databaseProperties;
    }

    public String getTMDBAPIUrl() throws IOException {
        return getConfigProperties().getProperty("tmdb.link.api");
    }

    public String getTMDBImageUrl() throws IOException {
        return getConfigProperties().getProperty("tmdb.link.api_img");
    }

    public String getTMDBToken() throws IOException {
        return getConfigProperties().getProperty("tmdb.token");
    }

    public String getOMDBToken() throws IOException {
        return getConfigProperties().getProperty("omdb.token");
    }

    public String getOMDBAPIUrl() throws IOException {
        return getConfigProperties().getProperty("omdb.link.api");
    }
}
