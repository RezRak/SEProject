package swe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
