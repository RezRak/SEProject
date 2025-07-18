package swe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import swe.controllers.EditEmployee;

import java.sql.*;

public class AdminDashboardController {

    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    @FXML private Button generateReportButton;
    @FXML private Button addEmployeeButton;
    @FXML private VBox employeeContainer;

    private final String DB_URL = "jdbc:mysql://137.184.27.234:3306/time_tracking";
    private final String DB_USER = "swe_user";
    private final String DB_PASSWORD = "@2025SWE";

    @FXML
    public void initialize() {
        loadEmployees();
        addEmployeeButton.setOnAction(e -> EditEmployee.addEmployee());
        // Generate report logic still unimplemented
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

                Label info = new Label(
                    String.format("%d | %s | %s | %s", id, username, fullName, email)
                );

                Button changeRate = new Button("Change Hourly Rate");
                changeRate.setOnAction(e -> EditEmployee.editEmployeeHourlyRate(id));

                Button changePassword = new Button("Change Password");
                changePassword.setOnAction(e -> EditEmployee.editEmployeePassword(id));

                Button removeEmployee = new Button("Remove");
                removeEmployee.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Delete");
                    alert.setHeaderText("Are you sure you want to remove this employee?");
                    alert.setContentText("This action cannot be undone.");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            boolean success = EditEmployee.removeEmployee(id);
                            if (success) {
                                loadEmployees(); // refresh UI
                            } else {
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setTitle("Error");
                                errorAlert.setHeaderText("Failed to delete employee.");
                                errorAlert.setContentText("Please check the database connection.");
                                errorAlert.showAndWait();
                            }
                        }
                    });
                });

                row.getChildren().addAll(info, changeRate, changePassword, removeEmployee);
                employeeContainer.getChildren().add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}