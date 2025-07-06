package swe.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import swe.Database;

public class PunchController {
    public boolean isPunchedIn(int userId) throws SQLException {
        Connection conn = Database.getConnection();
        String sql = "SELECT * FROM punch_logs WHERE user_id = ? AND punch_out IS NULL;";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, userId);

        ResultSet rs = statement.executeQuery();
        return rs.next();
    }
}
