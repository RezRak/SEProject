package swe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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

        // Temporarily disable this line to fix build error
        // addEmployeeButton.setOnAction(e -> EditEmployee.addEmployee());
        // Report button left unimplemented for now
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
                // Temporarily disable to prevent error
                // changeRate.setOnAction(e -> EditEmployee.editEmployeeHourlyRate(id));

                Button changePassword = new Button("Change Password");
                // Temporarily disable to prevent error
                // changePassword.setOnAction(e -> EditEmployee.editEmployeePassword(id));

                row.getChildren().addAll(info, changeRate, changePassword);
                employeeContainer.getChildren().add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
