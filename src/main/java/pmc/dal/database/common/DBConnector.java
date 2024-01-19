package pmc.dal.database.common;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {
    private static final String PROP_FILE = "config/config.settings";
    private final SQLServerDataSource dataSource;

    public DBConnector() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(PROP_FILE));

        dataSource = new SQLServerDataSource();
        dataSource.setServerName(properties.getProperty("db.server"));
        dataSource.setDatabaseName(properties.getProperty("db.database"));

        if (Boolean.parseBoolean(properties.getProperty("db.use_integrated_security"))) {
            dataSource.setIntegratedSecurity(true);
        } else {
            dataSource.setUser(properties.getProperty("db.username"));
            dataSource.setPassword(properties.getProperty("db.password"));
        }

        dataSource.setPortNumber(Integer.parseInt(properties.getProperty("db.port")));
        dataSource.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }
}
