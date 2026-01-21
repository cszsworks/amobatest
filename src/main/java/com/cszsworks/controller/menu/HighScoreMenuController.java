package com.cszsworks.controller.menu;

import com.cszsworks.persistence.model.HighScoreDisplayEntry;
import com.cszsworks.view.LanternaHighScoreRenderer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreMenuController {

    private static final String DB_URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7814323";
    private static final String DB_USER = "sql7814323";
    private static final String DB_PASS = "W3Ey4JZ2ae";

    private final LanternaHighScoreRenderer renderer;

    public HighScoreMenuController(LanternaHighScoreRenderer renderer) {
        this.renderer = renderer;
    }

    /** Fetch top 10 highscores grouped by username */
    public List<HighScoreDisplayEntry> getTopHighScores() {
        List<HighScoreDisplayEntry> results = new ArrayList<>();

        String sql = "SELECT username, MAX(highscore) AS best_score " +
                "FROM highscores " +
                "GROUP BY username " +
                "ORDER BY best_score DESC " +
                "LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new HighScoreDisplayEntry(
                        rs.getString("username"),
                        rs.getInt("best_score")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /** Show highscores via GUI2 renderer */
    public void showHighScores() {
        List<HighScoreDisplayEntry> scores = getTopHighScores();
        renderer.render(scores); // now includes a close button
        // after window closes, control automatically returns
    }

}
