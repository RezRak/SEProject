package swe.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import swe.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button punchButton;

    private int userId;
    private String username;

    // Set user info when loading the dashboard
    public void setUserInfo(int userId, String username) {
        this.userId = userId;
        this.username = username;
        welcomeLabel.setText("Welcome, " + username + "!");
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

            // Optionally: print or log updated punch history
            List<PunchHistory> history = getPunchHistoryForEmployee(userId);
            history.forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
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
}