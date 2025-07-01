package swe;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        Connection conn = Database.getConnection();
        if (conn != null) {
            System.out.println("Database connection successful!");
        } else {
            System.out.println("Database connection failed.");
        }
    }
}