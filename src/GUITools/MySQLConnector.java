package GUITools;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnector {
    private static MySQLConnector instance = null;
    private Connection connection;

    private MySQLConnector() {
        // Private constructor to prevent instantiation from outside
        String url = "jdbc:mysql://localhost:3306/PROM";
        String username = "root";
        String password = "polytech2023";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Failed to create MySQL connection");
            e.printStackTrace();
        }
    }

    public static MySQLConnector getInstance() {
        if (instance == null) {
            instance = new MySQLConnector();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }

    public void disconnect() throws SQLException {
        connection.close();
        System.out.println("Disconnected from MySQL database.");
    }
}
