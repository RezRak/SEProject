package swe.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import swe.model.WorkSummary;

public class EmployeeData {
    private final String DB_URL = "jdbc:mysql://137.184.27.234:3306/time_tracking";
    private final String DB_USER = "swe_user";
    private final String DB_PASSWORD = "@2025SWE";

    public WorkSummary returnEmployeeData(int id) {
            float hoursForWeek = 0;
            float payForWeek = 0;

            LocalDate today = LocalDate.now();
            DayOfWeek firstDayOfWeek = DayOfWeek.SUNDAY;
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
            LocalDateTime startDateTime = startOfWeek.atStartOfDay();
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            LocalDateTime endDateTime = endOfWeek.atTime(LocalTime.MAX);

            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT ROUND(SUM(TIMESTAMPDIFF(SECOND, pl.punch_in, pl.punch_out) / 3600.0), 2) AS total_hours," +
                " ROUND(SUM(TIMESTAMPDIFF(SECOND, pl.punch_in, pl.punch_out) / 3600.0 * u.hourly_rate), 2) AS total_pay" +
                " FROM punch_logs pl JOIN users u ON pl.user_id = u.id WHERE pl.user_id = ? AND pl.punch_out BETWEEN ? AND ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setTimestamp(2, startTimestamp);
            stmt.setTimestamp(3, endTimestamp);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                hoursForWeek = rs.getFloat("total_hours");
                payForWeek = rs.getFloat("total_pay");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new WorkSummary(startOfWeek, endOfWeek, hoursForWeek, payForWeek);
    }
}
