package swe.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PunchController {
    private final Connection conn;

    public PunchController(Connection conn) {
        this.conn = conn;
    }

    // Public method to toggle punch status
    public void updatePunchStatus(int userId) throws SQLException {
        if (isPunchedIn(userId)) {
            punchOut(userId);
        } else {
            punchIn(userId);
        }
    }

    // Checks if user is currently punched in
    public boolean isPunchedIn(int userId) throws SQLException {
        String sql = "SELECT 1 FROM punch_logs WHERE user_id = ? AND punch_out IS NULL";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Record punch in
    private void punchIn(int userId) throws SQLException {
        String sql = "INSERT INTO punch_logs (user_id, punch_in) VALUES (?, NOW())";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    // Record punch out
    private void punchOut(int userId) throws SQLException {
        String sql = "UPDATE punch_logs SET punch_out = NOW() WHERE user_id = ? AND punch_out IS NULL";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }
}