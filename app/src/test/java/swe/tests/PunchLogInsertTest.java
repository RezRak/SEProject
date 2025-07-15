package swe.tests;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class PunchLogInsertTest {
    @Test
    public void testPunchInOutCycle() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://137.184.27.234:3306/time_tracking", "swe_user", "@2025SWE");

        int userId = 1;

        // Punch in
        String insertSql = "INSERT INTO punch_logs (user_id, punch_in) VALUES (?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setInt(1, userId);
        insertStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        insertStmt.executeUpdate();
        ResultSet keys = insertStmt.getGeneratedKeys();
        assertTrue(keys.next(), "Punch-in should generate a new row");

        int punchId = keys.getInt(1);

        // Punch out
        String updateSql = "UPDATE punch_logs SET punch_out = ? WHERE id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
        updateStmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().plusHours(4)));
        updateStmt.setInt(2, punchId);
        int updated = updateStmt.executeUpdate();
        assertEquals(1, updated, "Punch-out should update the same row");

        conn.close();
    }
}