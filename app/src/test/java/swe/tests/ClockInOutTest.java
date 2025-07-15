package swe.tests;

import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ClockInOutTest {
    @Test
    public void testClockIn() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://137.184.27.234:3306/time_tracking", "swe_user", "@2025SWE");

        String insertSql = "INSERT INTO punch_logs (user_id, punch_in) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setInt(1, 1); // ajohnson
        insertStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

        int result = insertStmt.executeUpdate();
        assertEquals(1, result, "Clock-in should insert one row");
        conn.close();
    }
}

