package swe.tests;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    @Test
    public void testAdminLogin() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://137.184.27.234:3306/time_tracking", "swe_user", "@2025SWE");
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "admin1");
        stmt.setString(2, "adminpasshash");
        ResultSet rs = stmt.executeQuery();
        assertTrue(rs.next(), "Admin should be able to log in");
        conn.close();
    }
}
