package swe.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AdminDashboardController {

    @FXML private Button generateReportButton;
    @FXML private Button addEmployeeButton;
    @FXML private VBox employeeContainer;

    private final String DB_URL = "jdbc:mysql://137.184.27.234:3306/time_tracking";
    private final String DB_USER = "swe_user";
    private final String DB_PASSWORD = "@2025SWE";

    @FXML
    public void initialize() {
        loadEmployees();

        addEmployeeButton.setOnAction(e -> {
            boolean added = EditEmployee.addEmployee(0); // prompt for all fields
            if (added) {
                loadEmployees(); // Refresh list
            }
        });

        generateReportButton.setOnAction(e -> openReportView());
    }

    private void openReportView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/report.fxml"));
            Parent reportRoot = loader.load();
            Stage reportStage = new Stage();
            reportStage.setTitle("Payroll Report");
            reportStage.setScene(new Scene(reportRoot));
            reportStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading report view: " + e.getMessage());
        }
    }

    private void loadEmployees() {
        employeeContainer.getChildren().clear();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT id, username, full_name, email FROM users WHERE role = 'employee'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");

                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: white;");

                Label info = new Label(String.format("%d | %s | %s | %s", id, username, fullName, email));

                Button changeRate = new Button("Change Hourly Rate");
                changeRate.setOnAction(e -> {
                    boolean updated = EditEmployee.editEmployeeHourlyRate(id, 0); // prompt for rate
                    if (updated) loadEmployees();
                });

                Button changePassword = new Button("Change Password");
                changePassword.setOnAction(e -> {
                    boolean updated = EditEmployee.editEmployeePassword(id);
                    if (updated) loadEmployees();
                });

                Button removeEmployee = new Button("Remove");
                removeEmployee.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Delete");
                    alert.setHeaderText("Are you sure you want to remove this employee?");
                    alert.setContentText("This action cannot be undone.");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            boolean success = EditEmployee.removeEmployee(id);
                            if (success) loadEmployees();
                            else showAlert("Failed to delete employee. Check database connection.");
                        }
                    });
                });

                row.getChildren().addAll(info, changeRate, changePassword, removeEmployee);
                employeeContainer.getChildren().add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) generateReportButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load login screen: " + e.getMessage());
        }
    }
}