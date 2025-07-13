package swe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final String DB_URL = "jdbc:mysql://137.184.27.234:3306/time_tracking";
    private final String DB_USER = "swe_user";
    private final String DB_PASSWORD = "@2025SWE";
    private final String ADMIN_VIEW_PATH = "/views/admin_dashboard.fxml";
    private final String ADMIN_VIEW_TITLE = "Administrator Dashboard";
    private final String EMP_VIEW_PATH = "/views/employee_dashboard.fxml";
    private final String EMP_VIEW_TITLE = "Employee Dashboard";

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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Action Failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goToAdminDashboard() {
        changeView(ADMIN_VIEW_PATH, ADMIN_VIEW_TITLE);
    }

    private void goToEmployeeDashboard() {
        changeView(EMP_VIEW_PATH, EMP_VIEW_TITLE);
    }

    private void changeView(String path, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(path));
            Scene newScene = new Scene(parent);
            Stage currenStage = (Stage) usernameField.getScene().getWindow();
            currenStage.setTitle(title);
            currenStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
