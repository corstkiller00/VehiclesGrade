package org.exampl.vehicles.Database;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private final JavaPlugin plugin;
    private Connection connection;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() throws SQLException {

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File database = new File(plugin.getDataFolder(), "ships.db");

        connection = DriverManager.getConnection("jdbc:sqlite:" + database.getAbsolutePath());

        try (Statement statement = connection.createStatement()) {

            statement.execute("""
                    PRAGMA foreign_keys = ON;
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS ships (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL UNIQUE
                    );
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS ship_blocks (
                        ship_id INTEGER NOT NULL,
                        world_uuid TEXT NOT NULL,
                        x INTEGER NOT NULL,
                        y INTEGER NOT NULL,
                        z INTEGER NOT NULL,
                        material TEXT NOT NULL,

                        PRIMARY KEY (world_uuid, x, y, z),

                        FOREIGN KEY (ship_id)
                            REFERENCES ships(id)
                            ON DELETE CASCADE
                    );
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS ship_armour_stands (
                        uuid TEXT PRIMARY KEY,
                        ship_id INTEGER NOT NULL,
                        world_uuid TEXT NOT NULL,
                        x DOUBLE NOT NULL,
                        y DOUBLE NOT NULL,
                        z DOUBLE NOT NULL,

                        FOREIGN KEY (ship_id)
                            REFERENCES ships(id)
                            ON DELETE CASCADE
                    );
                    """);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ignored) {
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // ------------------------------------------------------------------------
    // Ships
    // ------------------------------------------------------------------------

    public int createShip(String name) throws SQLException {

        String sql = "INSERT INTO ships(name) VALUES(?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Failed to create ship.");
    }

    public Integer getShipId(String name) throws SQLException {

        String sql = "SELECT id FROM ships WHERE name = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return null;
    }

    public void deleteShip(int shipId) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM ships WHERE id = ?")) {

            ps.setInt(1, shipId);
            ps.executeUpdate();
        }
    }

    // ------------------------------------------------------------------------
    // Blocks
    // ------------------------------------------------------------------------

    public void addBlock(int shipId, World world, int x, int y, int z, Material material) throws SQLException {

        String sql = """
                INSERT OR REPLACE INTO ship_blocks
                (ship_id, world_uuid, x, y, z, material)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, shipId);
            ps.setString(2, world.getUID().toString());
            ps.setInt(3, x);
            ps.setInt(4, y);
            ps.setInt(5, z);
            ps.setString(6, material.name());

            ps.executeUpdate();
        }
    }

    public void removeBlock(World world, int x, int y, int z) throws SQLException {

        String sql = """
                DELETE FROM ship_blocks
                WHERE world_uuid = ?
                AND x = ?
                AND y = ?
                AND z = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, world.getUID().toString());
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, z);

            ps.executeUpdate();
        }
    }

    // ------------------------------------------------------------------------
    // Armour Stands
    // ------------------------------------------------------------------------

    public void addArmourStand(int shipId, ArmorStand stand) throws SQLException {

        String sql = """
                INSERT OR REPLACE INTO ship_armour_stands
                (uuid, ship_id, world_uuid, x, y, z)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, stand.getUniqueId().toString());
            ps.setInt(2, shipId);
            ps.setString(3, stand.getWorld().getUID().toString());
            ps.setDouble(4, stand.getLocation().getX());
            ps.setDouble(5, stand.getLocation().getY());
            ps.setDouble(6, stand.getLocation().getZ());

            ps.executeUpdate();
        }
    }

    public void removeArmourStand(UUID uuid) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM ship_armour_stands WHERE uuid = ?")) {

            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        }
    }
}
