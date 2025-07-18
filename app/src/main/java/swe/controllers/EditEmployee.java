package swe.controllers;
import java.sql.*;

public class EditEmployee {

    private static final String url = "jdbc:mysql://137.184.27.234:3306/time_tracking";
    private static final String username = "swe_user";
    private static final String password = "@2025SWE";


    public boolean editEmployeePassword(int user_id, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE user_id = ?";

         try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

                statement.setString(1, newPassword);
                statement.setInt(2, user_id);

                int rowsUpdated = statement.executeUpdate();
                return rowsUpdated > 0;

             } catch (SQLException e) {
                e.printStackTrace();
                return false;
             }
             

    }

    public static boolean addEmployee(String username, String full_name, String email, String password_hash, String role, double hourly_rate) {
        String query = "INSERT INTO users (username, full_name, email, password_hash, role, hourly_rate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

                statement.setString(1, username);
                statement.setString(2, full_name);
                statement.setNString(3, email);
                statement.setString(4, password_hash);
                statement.setString(5, role);
                statement.setDouble(6, hourly_rate);

                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;

             } catch (SQLException e) {
                e.printStackTrace();
                return false;
             }
    }

    public boolean editEmployeeHourlyRate(int user_id, double newHourlyRate) {
        String query = "UPDATE users SET hourly_rate = ? WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

                statement.setInt(1, user_id);
                statement.setDouble(2, newHourlyRate);

                int rowsUpdated = statement.executeUpdate();
                return rowsUpdated > 0;

             } catch(SQLException e) {
                e.printStackTrace();
                return false;
             }
    }
      public static boolean removeEmployee(int user_id) {
         String query = "DELETE FROM users WHERE id = ? AND role = 'employee'";

         try (Connection conn = DriverManager.getConnection(url, username, password);
               PreparedStatement statement = conn.prepareStatement(query)) {

               statement.setInt(1, user_id);
               int rowsDeleted = statement.executeUpdate();
               return rowsDeleted > 0;

         } catch (SQLException e) {
               e.printStackTrace();
               return false;
         }
      }
    
}
