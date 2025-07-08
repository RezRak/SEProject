package swe.controllers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDashboardController {

           private static final String url = "jdbc:mysql://137.184.27.234:3306/time_tracking";
           private static final String username = "swe_user";
           private static final String password = "@2025SWE";

           public static class PunchHistory {
            private String punchIn;
            private String punchOut;
           }

           public PunchHistory(String punchIn, String punchOut) {
            this.punchIN = punchIn;
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
            return "Punch In: " + punchIn + "Punch Out: " + punchOut;

           }
}

    public List<PunchHistory> getPunchHistoryForEmployee(int user_id) {
        List<PunchHistory> historyList = new ArrayList<>();

        String query = "SELECT * FROM punch_logs WHERE user_id = 1 ORDER BY punch_in DESC";

        try (Connection conn = DriverManager.getConnection(url, username, password);
            PrepareStatement statement = conn.prepareStatement(query)) {

                statement.setInt(1, user_id);
                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    String punchIn = result.getString("punch_in");
                    String punchOut = result.getString("punch_out");

                    PunchHistory history = new PunchHistory(punchIn, punchOut);
                    historyList.add(history);
                }
                
            } catch (SQLExeption e) {
                e.printStackTrace();
         }

            return historyList;
    }