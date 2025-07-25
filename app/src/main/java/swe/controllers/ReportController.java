package swe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import swe.Database;

public class ReportController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<PayrollReport> reportTable;

    @FXML
    private TableColumn<PayrollReport, String> employeeColumn;

    @FXML
    private TableColumn<PayrollReport, String> periodStartColumn;

    @FXML
    private TableColumn<PayrollReport, String> periodEndColumn;

    @FXML
    private TableColumn<PayrollReport, Double> totalHoursColumn;

    @FXML
    private TableColumn<PayrollReport, Double> hourlyRateColumn;

    @FXML
    private TableColumn<PayrollReport, Double> totalPayColumn;

    @FXML
    private void initialize() {
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        periodStartColumn.setCellValueFactory(new PropertyValueFactory<>("periodStart"));
        periodEndColumn.setCellValueFactory(new PropertyValueFactory<>("periodEnd"));
        totalHoursColumn.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
        hourlyRateColumn.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        totalPayColumn.setCellValueFactory(new PropertyValueFactory<>("totalPay"));
    }

    @FXML
    private void generateReport(ActionEvent event) {
        reportTable.getItems().clear();

        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showAlert("Please select both start and end dates!");
            return;
        }

        String startDate = startDatePicker.getValue().toString();
        String endDate = endDatePicker.getValue().toString();

        calculateAndInsertPayroll(startDate, endDate); // ← Auto-inserts records before fetching

        String sql = "SELECT u.full_name, pr.period_start, pr.period_end, pr.total_hours, u.hourly_rate, pr.total_pay " +
                     "FROM payroll_reports pr " +
                     "JOIN users u ON pr.user_id = u.id " +
                     "WHERE pr.period_start >= ? AND pr.period_end <= ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            ResultSet rs = stmt.executeQuery();
            ObservableList<PayrollReport> data = FXCollections.observableArrayList();

            while (rs.next()) {
                String employeeName = rs.getString("full_name");
                String periodStart = rs.getString("period_start");
                String periodEnd = rs.getString("period_end");
                double totalHours = rs.getDouble("total_hours");
                double hourlyRate = rs.getDouble("hourly_rate");
                double totalPay = rs.getDouble("total_pay");

                data.add(new PayrollReport(employeeName, periodStart, periodEnd, totalHours, hourlyRate, totalPay));
            }

            reportTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error:\n" + e.getMessage());
        }
    }

    private void calculateAndInsertPayroll(String startDate, String endDate) {
        String punchQuery = """
            SELECT 
                u.id AS user_id,
                u.full_name,
                u.hourly_rate,
                SUM(TIMESTAMPDIFF(SECOND, p.punch_in, p.punch_out)) / 3600 AS total_hours
            FROM punch_logs p
            JOIN users u ON p.user_id = u.id
            WHERE p.punch_in >= ? AND p.punch_out <= ? AND u.role = 'employee'
            GROUP BY u.id
        """;

        String insertQuery = """
            INSERT INTO payroll_reports (user_id, period_start, period_end, total_hours, total_pay)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE total_hours = VALUES(total_hours), total_pay = VALUES(total_pay)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement punchStmt = conn.prepareStatement(punchQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            punchStmt.setString(1, startDate);
            punchStmt.setString(2, endDate);

            ResultSet rs = punchStmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                double hours = rs.getDouble("total_hours");
                double rate = rs.getDouble("hourly_rate");
                double pay = hours * rate;

                insertStmt.setInt(1, userId);
                insertStmt.setString(2, startDate);
                insertStmt.setString(3, endDate);
                insertStmt.setDouble(4, hours);
                insertStmt.setDouble(5, pay);
                insertStmt.addBatch();
            }

            insertStmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error calculating payroll: " + e.getMessage());
        }
    }

    @FXML
    private void exportToCSV(ActionEvent event) {
        ObservableList<PayrollReport> data = reportTable.getItems();

        if (data.isEmpty()) {
            showAlert("No data to export. Please generate a report first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Employee Name,Period Start,Period End,Total Hours,Hourly Rate,Total Pay\n");

                for (PayrollReport report : data) {
                    writer.write(String.format("%s,%s,%s,%.2f,%.2f,%.2f\n",
                            report.getEmployeeName(),
                            report.getPeriodStart(),
                            report.getPeriodEnd(),
                            report.getTotalHours(),
                            report.getHourlyRate(),
                            report.getTotalPay()));
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText(null);
                alert.setContentText("Report exported successfully to:\n" + file.getAbsolutePath());
                alert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error saving CSV:\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}