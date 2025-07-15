package swe.tests;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminLoginRedirectTest {
    @Test
    public void testAdminRoleRedirect() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://137.184.27.234:3306/time_tracking", "swe_user", "@2025SWE");
        String sql = "SELECT role FROM users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "admin1");
        ResultSet rs = stmt.executeQuery();
        assertTrue(rs.next(), "Admin user should exist");
        assertEquals("admin", rs.getString("role"), "Should redirect to admin dashboard");
        conn.close();
    }
}