package swe.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import swe.Database;

public class PunchController {
    private final Connection conn;

    public PunchController(Connection conn) {
        this.conn = conn;
    }

    public void updatePunchStatus(int userId) throws SQLException {
        if (isPunchedIn(userId)) {
            punchOut(userId);
        } else {
            punchIn(userId);
        }
    }

    boolean isPunchedIn(int userId) throws SQLException {
        String sql = "SELECT * FROM punch_logs WHERE user_id = ? AND punch_out IS NULL;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, userId);

        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    private void punchIn(int userId) throws SQLException {
        String sql = "INSERT INTO punch_logs (user_id, punch_in) VALUES (?, NOW());";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, userId);
        statement.executeUpdate();
    }

    private void punchOut(int userId) throws SQLException {
        String sql = "UPDATE punch_logs SET punch_out = NOW() WHERE user_id = ? AND punch_out IS NULL;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, userId);
        statement.executeUpdate();
    }
}