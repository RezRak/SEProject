package swe.controllers;

import javafx.application.Platform;
import javafx.scene.control.*;
import java.sql.*;
import java.util.Optional;

public class EditEmployee {

    private static final String url = "jdbc:mysql://137.184.27.234:3306/time_tracking";
    private static final String username = "swe_user";
    private static final String password = "@2025SWE";

    public static boolean editEmployeePassword(int userId) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Password");
        dialog.setHeaderText("Change Password for Employee ID: " + userId);
        dialog.setContentText("Enter new password:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) {
            showAlert("Action cancelled: Invalid input");
            return false;
        }

        String newPassword = result.get().trim();
        String query = "UPDATE users SET password_hash = ? WHERE id = ? AND role = 'employee'";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, newPassword);
            statement.setInt(2, userId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showInfo("Password changed successfully");
                return true;
            } else {
                showAlert("Invalid input/Employee not found");
                return false;
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean addEmployee(double hourlyRate) {
        TextInputDialog dialog1 = new TextInputDialog();
        dialog1.setTitle("Add Employee");
        dialog1.setHeaderText(null);
        dialog1.setContentText("Enter username:");
        Optional<String> usernameInput = dialog1.showAndWait();

        TextInputDialog dialog2 = new TextInputDialog();
        dialog2.setTitle("Add Employee");
        dialog2.setContentText("Enter full name:");
        Optional<String> fullName = dialog2.showAndWait();

        TextInputDialog dialog3 = new TextInputDialog();
        dialog3.setTitle("Add Employee");
        dialog3.setContentText("Enter email:");
        Optional<String> email = dialog3.showAndWait();

        TextInputDialog dialog4 = new TextInputDialog();
        dialog4.setTitle("Add Employee");
        dialog4.setContentText("Enter password:");
        Optional<String> passwordHash = dialog4.showAndWait();

        TextInputDialog dialog5 = new TextInputDialog("employee");
        dialog5.setTitle("Add Employee");
        dialog5.setContentText("Enter role:");
        Optional<String> role = dialog5.showAndWait();

        TextInputDialog dialog6 = new TextInputDialog();
        dialog6.setTitle("Add Employee");
        dialog6.setContentText("Enter hourly rate:");
        Optional<String> rate = dialog6.showAndWait();

        if (usernameInput.isEmpty() || fullName.isEmpty() || email.isEmpty() || passwordHash.isEmpty() ||
            role.isEmpty() || rate.isEmpty()) {
            showAlert("Action cancelled: Incomplete input");
            return false;
        }

        try {
            hourlyRate = Double.parseDouble(rate.get());
        } catch (NumberFormatException e) {
            showAlert("Invalid hourly rate format");
            return false;
        }

        String query = "INSERT INTO users (username, full_name, email, password_hash, role, hourly_rate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, usernameInput.get());
            statement.setString(2, fullName.get());
            statement.setString(3, email.get());
            statement.setString(4, passwordHash.get());
            statement.setString(5, role.get());
            statement.setDouble(6, hourlyRate);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                showInfo("Employee added successfully");
                return true;
            } else {
                showAlert("Failed to add employee");
                return false;
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean editEmployeeHourlyRate(int userId, double newRateValue) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Hourly Rate");
        dialog.setHeaderText("Change Hourly Rate for Employee ID: " + userId);
        dialog.setContentText("Enter new rate:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) {
            showAlert("Action cancelled: Invalid input");
            return false;
        }

        try {
            newRateValue = Double.parseDouble(result.get().trim());
        } catch (NumberFormatException e) {
            showAlert("Invalid hourly rate format");
            return false;
        }

        String query = "UPDATE users SET hourly_rate = ? WHERE id = ? AND role = 'employee'";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setDouble(1, newRateValue);
            statement.setInt(2, userId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                showInfo("Hourly rate updated successfully");
                return true;
            } else {
                showAlert("Invalid input/Employee not found");
                return false;
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean removeEmployee(int userId) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Remove Employee ID: " + userId);
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            showInfo("Action cancelled");
            return false;
        }

        String query = "DELETE FROM users WHERE id = ? AND role = 'employee'";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                showInfo("Employee removed successfully");
                return true;
            } else {
                showAlert("Failed to remove employee");
                return false;
            }
        } catch (SQLException e) {
            showAlert("Error: " + e.getMessage());
            return false;
        }
    }

    private static void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private static void showInfo(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}