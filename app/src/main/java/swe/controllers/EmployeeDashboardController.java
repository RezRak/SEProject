package swe.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import swe.Database;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button punchButton;
    @FXML private TableView<PunchHistory> punchHistoryTable;
    @FXML private TableColumn<PunchHistory, String> punchInCol;
    @FXML private TableColumn<PunchHistory, String> punchOutCol;

    private int userId;
    private String username;

    // Set user info when loading the dashboard
    public void setUserInfo(int userId, String username) {
        this.userId = userId;
        this.username = username;
        welcomeLabel.setText("Welcome, " + username + "!");
        loadPunchHistory(); // Load history when setting user
    }

    @FXML
    public void initialize() {
        punchInCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPunchIn()));
        punchOutCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPunchOut()));
    }

    @FXML
    private void handlePunch() {
        try (Connection conn = Database.getConnection()) {
            if (conn == null) {
                showAlert("Failed to connect to the database.");
                return;
            }

            PunchController punchController = new PunchController(conn);
            punchController.updatePunchStatus(userId);

            if (punchController.isPunchedIn(userId)) {
                showInfo("You are now clocked in.");
            } else {
                showInfo("You have clocked out.");
            }

            loadPunchHistory(); // Reload history after punch

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
    }

    private void loadPunchHistory() {
        List<PunchHistory> history = getPunchHistoryForEmployee(userId);
        punchHistoryTable.getItems().setAll(history);
    }

    public static class PunchHistory {
        private final String punchIn;
        private final String punchOut;

        public PunchHistory(String punchIn, String punchOut) {
            this.punchIn = punchIn;
            this.punchOut = punchOut;
        }

        public String getPunchIn() {
            return punchIn;
        }

        public String getPunchOut() {
            return punchOut;
        }

        @Override
        public String toString() {
            return "Punch In: " + punchIn + " | Punch Out: " + punchOut;
        }
    }

    public List<PunchHistory> getPunchHistoryForEmployee(int userId) {
        List<PunchHistory> historyList = new ArrayList<>();
        String query = "SELECT * FROM punch_logs WHERE user_id = ? ORDER BY punch_in DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String punchIn = result.getString("punch_in");
                String punchOut = result.getString("punch_out");

                PunchHistory history = new PunchHistory(punchIn, punchOut);
                historyList.add(history);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
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

    @FXML
    private void handleLogout() {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent loginRoot = loader.load();

            // Set the scene on the current stage
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load login screen: " + e.getMessage());
        }
    }
}