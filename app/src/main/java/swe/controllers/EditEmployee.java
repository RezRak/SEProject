package swe.controllers;
import java.sql.*;
import javax.swing.*;

public class EditEmployee {

    private static final String url = "jdbc:mysql://137.184.27.234:3306/time_tracking";
    private static final String username = "swe_user";
    private static final String password = "@2025SWE";


    public boolean editEmployeePassword(int user_id, String newPassword) {
         String newPassword = JOptionPane.showInputDialog(null, "Enter new password for employee: " + user_id);

         if (newPassword == null || newPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Action cancelled: Invalid input");
            return;
         }

        String query = "UPDATE users SET password = ? WHERE user_id = ? AND role = 'employee'";

         try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

                statement.setString(1, newPassword);
                statement.setInt(2, user_id);

                int rowsUpdated = statement.executeUpdate();
                
                if (return rowsUpdated > 0) {
                  JOptionPane.showMessageDialog(null, "Password changed successfully");
                } else {
                  JOptionPane.showMessageDialog(null, "Invalid input/Employee not found");
                }

             } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
             }
             
    }

    public static boolean addEmployee(double hourly_rate) {
        String username = JOptionPane.showInputDialog("Enter employee username: ");
        String full_name = JOptionPane.showInputDialog("Enter employee full name: ");
        String email = JOptionPane.showInputDialog("Enter employee email: ");
        String password_hash = JOptionPane.showInputDialog("Enter employee password: ");
        String role = JOptionPane.showInputDialog("Enter employee role: ");
        String hourlyRateString = JOptionPane.showInputDialog("Enter employee hourly rate: ");

        if (username == null || full_name == null || email == null || password_hash == null || 
        role == null || hourlyRateString == null || username.trim().isEmpty() || full_name.trim().isEmpty() ||
        email.trim().isEmpty() || password_hash.trim().isEmpty() || role.trim().isEmpty() || hourlyRateString.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "Action cancelled: Invalid input");
         return;
        }

        try {
            hourly_rate = Double.parseDouble(hourlyRateString);
         } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Action cancelled: Incorrect rate format");
            return;
         }

        String query = "INSERT INTO users (username, full_name, email, password_hash, role, hourly_rate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

                statement.setString(1, username);
                statement.setString(2, full_name);
                statement.setString(3, email);
                statement.setString(4, password_hash);
                statement.setString(5, role);
                statement.setDouble(6, hourly_rate);

                int rowsInserted = statement.executeUpdate();
                
                if (return rowsInserted > 0) {
                  JOptionPane.showMessageDialog(null, "Employee added successfully.");
                } else {
                  JOptionPane.showMessageDialog(null, "Failed to add employee.");
                }

             } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
             }
    }

    public boolean editEmployeeHourlyRate(int user_id, double newHourlyRate) {
        String newRate = JOptionPane.showInputDialog(null, "Enter new hourly rate for employee: " + user_id);
        
        if (newRate == null || newRate.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "Action cancelled: Invalid input");
         return;
        }
         try {
            newHourlyRate = Double.parseDouble(newRate);
         } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Action cancelled: Incorrect rate format");
            return;
         }

        
        String query = "UPDATE users SET hourly_rate = ? WHERE user_id = ? AND role = 'emplyee'";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

                statement.setInt(1, user_id);
                statement.setDouble(2, newHourlyRate);

                int rowsUpdated = statement.executeUpdate();
                
                if (return rowsUpdated > 0) {
                  JOptionPane.showMessageDialog(null, "Hourly Rate updated successfully");
                } else {
                  JOptionPane.showMessageDialog(null, "Invalid input/Employee not found");
                }

             } catch(SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
             }
      }
      public static boolean removeEmployee(int user_id) {
         String deleteEmployee = JOptionPane.showInputDialog("Enter employee to be removed: ");

         if (deleteEmployee == null || deleteEmployee.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Action cancelled: Invalid input");
            return;
         }

         String query = "DELETE FROM users WHERE user_id = ? AND role = 'employee'";

         try (Connection conn = DriverManager.getConnection(url, username, password);
               PreparedStatement statement = conn.prepareStatement(query)) {

               statement.setInt(1, user_id);
               int rowsDeleted = statement.executeUpdate();
              
               if (return rowsDeleted > 0) {
                  JOptionPane.showMessageDialog(null, "Employee removed successfully");
               } else {
                  JOptionPane.showMessageDialog(null, "Failed to remove employee");
               }

         } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
         }
      }
    
}
