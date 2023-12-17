package ru.ifmo.webservices.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, "Failed creating database connection", e);
            throw new RuntimeException(e);
        }
    }
    private DatabaseConnection() throws SQLException {
        Properties props = new Properties();
        try (var propsInputStream = DatabaseConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(propsInputStream);
        } catch (IOException e) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, "Failed loading properties file", e);
            throw new RuntimeException(e);
        }

        this.connection = DriverManager.getConnection(
            props.getProperty("database.url"),
            props.getProperty("database.username"),
            props.getProperty("database.password"));
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
