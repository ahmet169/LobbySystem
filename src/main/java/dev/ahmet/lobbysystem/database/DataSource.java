package dev.ahmet.lobbysystem.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.ahmet.lobbysystem.LobbySystem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static HikariConfig hikariConfig = new HikariConfig();
    private static HikariDataSource ds;

    static {
        FileConfiguration config = LobbySystem.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("mysql");
        String host = section.getString("host");
        int port = section.getInt("port");
        String database = section.getString("database");
        String username = section.getString("username");
        String password = section.getString("password");

        hikariConfig.setJdbcUrl("jdbc:mysql://"+ host +":" + port + "/"+ database);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(hikariConfig);
        if(ds.isRunning()) {
            LobbySystem.getInstance().getLogger().info("Datenbank Verbindung wurde hergestellt!");
        }
    }

    public DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
