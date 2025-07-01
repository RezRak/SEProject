package swe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://137.184.27.234:3306/swe_project";
            String username = "swe_user";
            String password = "@2025SWE";

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}