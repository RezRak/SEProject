package swe.tests;

import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeQueryTest {
    @Test
    public void testFindEmployee() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://137.184.27.234:3306/time_tracking", "swe_user",
                "@2025SWE");
        String sql = "SELECT full_name FROM users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "ajohnson");
        ResultSet rs = stmt.executeQuery();

        assertTrue(rs.next(), "Employee should exist");
        assertEquals("Alice Johnson", rs.getString("full_name"), "Name should match");
        conn.close();
    }
}

