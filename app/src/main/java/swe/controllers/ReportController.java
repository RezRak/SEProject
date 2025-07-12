package swe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

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
        System.out.println("Generate Report clicked!");
    }

    @FXML
    private void exit(ActionEvent event) {
        System.out.println("Exit clicked!");
    }
}
