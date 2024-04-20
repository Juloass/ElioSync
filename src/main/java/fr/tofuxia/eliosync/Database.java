package fr.tofuxia.eliosync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class Database {

    private static Connection connection = null;
    private static boolean prepared = false;

    /**
     * Connect to the database
     * 
     * @return true if the connection is successful or if the connection is already
     *         established otherwise false
     */
    public static boolean connect() {

        if (connection != null)
            return true;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Eliosync.LOGGER.error("Failed to load the MySQL driver: " + e.getMessage());
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Eliosync.LOGGER.error("Failed to load the MySQL driver: " + e.getMessage());
        }

        try {
            connection = DriverManager.getConnection(Config.jdbcUrl, Config.username, Config.password);
        } catch (SQLException e) {
            Eliosync.LOGGER.error("Failed to connect to the database: " + e.getMessage());
            Eliosync.LOGGER.warn(e.getSQLState() + " " + e.getErrorCode());
        }

        Eliosync.LOGGER.warn("Connected to the database ? {}", connection != null ? "✅" : "❌");
        return connection != null;
    }

    public static void prepare() {
        if (connection == null)
            return;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS player_data (uuid VARCHAR(36) NOT NULL, data TEXT(65535), UNIQUE KEY id (uuid) USING BTREE, PRIMARY KEY(uuid)) ENGINE=InnoDB;");
            statement.executeUpdate();

            statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS player_data_history (uuid VARCHAR(36), data TEXT(65535), timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (uuid, timestamp)) ENGINE=InnoDB;");
            statement.executeUpdate();

            prepared = true;
        } catch (SQLException e) {
            Eliosync.LOGGER.error("Failed to prepare the database: " + e.getMessage());
        }
    }

    public static boolean isReady() {
        if (connection == null)
            Eliosync.LOGGER.error("Connection is null");
        if (!prepared)
            Eliosync.LOGGER.error("Database is not prepared");
        return connection != null && prepared;
    }

    public static boolean contains(UUID uuid) {

        if (!isReady())
            throw new IllegalStateException("Database is not ready, this shoyld not happen if the server is running");

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT * FROM player_data WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            return statement.executeQuery().next();
        } catch (SQLException e) {
            Eliosync.LOGGER.error("Failed to prepare the statement: " + e.getMessage());
            return false;
        }

    }

    public static void store(UUID uuid, String data) {

        if (!isReady())
            throw new IllegalStateException("Database is not ready, this shoyld not happen if the server is running");

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(
                    "INSERT INTO player_data (uuid, data) VALUES (?, ?) ON DUPLICATE KEY UPDATE data = VALUES(data)");
            statement.setString(1, uuid.toString());
            statement.setString(2, data);
            statement.executeUpdate();
        } catch (SQLException e) {
            Eliosync.LOGGER.error("Failed to prepare the statement while storing current data: " + e.getMessage());
            Eliosync.LOGGER.debug(data);
        }

        try {
            statement = connection.prepareStatement("INSERT INTO player_data_history (uuid, data) VALUES (?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, data);
            statement.executeUpdate();
        } catch (SQLException e) {
            Eliosync.LOGGER.error("Failed to prepare the statement while storing backup data: " + e.getMessage());
            Eliosync.LOGGER.debug(data);
        }

    }

    public static Optional<String> load(UUID uuid) {

        if (!isReady())
            throw new IllegalStateException("Database is not ready, this should not happen if the server is running");

        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("SELECT data FROM player_data WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            return Optional.of(statement.executeQuery().getString("data"));
        } catch (SQLException e) {
            Eliosync.LOGGER.error("Failed to prepare the statement: " + e.getMessage());
            return Optional.empty();
        }
    }

}
