package dev.ahmet.lobbysystem.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    public void createTables() {
        try (Connection con = DataSource.getConnection(); PreparedStatement stmt = con.prepareStatement("CREATE TABLE IF NOT EXISTS lobbysystem (uuid VARCHAR(255),option VARCHAR(255), bool BOOLEAN)")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred", e);
        }
    }
}
