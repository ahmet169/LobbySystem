package dev.ahmet.lobbysystem.manager;

import com.sun.source.tree.UsesTree;
import dev.ahmet.lobbysystem.LobbySystem;
import dev.ahmet.lobbysystem.database.DataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LobbyManager {

    public void createPlayer(Player player) {
        List<String> setting = new ArrayList<>();
        setting.add("broadcast");
        setting.add("hider");
        for (String option : setting) {
            try(Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("INSERT INTO lobbysystem VALUES (?,?,?)")) {
                stmt.setString(1, player.getUniqueId().toString());
                stmt.setString(2, option);
                stmt.setBoolean(3, true);
                stmt.executeUpdate();
            } catch(SQLException e) {
                throw new RuntimeException();
            }
        }
    }

    public boolean isRegistered(Player player) {
        try(Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("SELECT * FROM lobbysystem WHERE uuid=?")) {
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("An error occured", e);
        }
        return false;
    }

    public boolean getOption(Player player, String option) {
        try(Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("SELECT * FROM lobbysystem WHERE uuid=? AND option=?")) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, option);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getBoolean("bool");
            }
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred", e);
        }
        return false;
    }

    public void updateOption(Player player, String option, boolean bool) {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("UPDATE lobbysystem SET bool=? WHERE uuid=? AND option=?")) {
            stmt.setBoolean(1, bool);
            stmt.setString(2, player.getUniqueId().toString());
            stmt.setString(3, option);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred", e);
        }
    }

    public Location getSpawnpoint() {
        FileConfiguration config = LobbySystem.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("spawnpoint");
        return new Location(Bukkit.getWorld("world"), section.getInt("x"), section.getInt("y"), section.getInt("z"), section.getLong("yaw"), section.getLong("pitch"));
    }

}
