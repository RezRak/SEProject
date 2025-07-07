package swe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;
import java.time.LocalDateTime;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField clockUsernameField;

    private final String DB_URL = "jdbc:mysql://137.184.27.234:3306/time_tracking";
    private final String DB_USER = "swe_user";
    private final String DB_PASSWORD = "@2025SWE";

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Both fields are required.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT password_hash, role FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String role = rs.getString("role");

                if (storedHash.equals(password)) {
                    if ("admin".equalsIgnoreCase(role)) {
                        goToAdminDashboard();
                    } else {
                        goToEmployeeDashboard();
                    }
                } else {
                    showAlert("Incorrect password.");
                }
            } else {
                showAlert("Username not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleClockInOut() {
        String username = clockUsernameField.getText().trim();
        if (username.isEmpty()) {
            showAlert("Please enter a username.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String getUserIdSql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(getUserIdSql);
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();

            if (!userRs.next()) {
                showAlert("Username not found.");
                return;
            }

            int userId = userRs.getInt("id");

            String checkLatestPunchSql = "SELECT id, punch_out FROM punch_logs WHERE user_id = ? ORDER BY punch_in DESC LIMIT 1";
            PreparedStatement checkStmt = conn.prepareStatement(checkLatestPunchSql);
            checkStmt.setInt(1, userId);
            ResultSet punchRs = checkStmt.executeQuery();

            if (punchRs.next()) {
                Timestamp punchOut = punchRs.getTimestamp("punch_out");

                if (punchOut == null) {
                    // perform punch out
                    int punchId = punchRs.getInt("id");
                    String updateSql = "UPDATE punch_logs SET punch_out = ? WHERE id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    updateStmt.setInt(2, punchId);
                    updateStmt.executeUpdate();
                    showInfo("Checked out successfully.");
                    return;
                }
            }

            // perform punch in
            String insertSql = "INSERT INTO punch_logs (user_id, punch_in) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, userId);
            insertStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            insertStmt.executeUpdate();
            showInfo("Checked in successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Action Failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goToAdminDashboard() {
        System.out.println("Redirect to Admin Dashboard...");
        // TODO: implement actual navigation
    }

    private void goToEmployeeDashboard() {
        System.out.println("Redirect to Employee Dashboard...");
        // TODO: implement actual navigation
    }
}
