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
    private AppState appState;
    private final LanternaHighScoreRenderer renderer;

    public HighScoreMenuController(LanternaHighScoreRenderer renderer) {
        this.renderer = renderer;
        this.appState = AppState.HIGH_SCORE_SCREEN;
    }

    //a kiirásra váró bejegyzések listája
    public List<HighScoreDisplayEntry> getTopHighScores() {

        List<HighScoreDisplayEntry> results = new ArrayList<>();

        String sql =
                "SELECT username, MAX(highscore) AS best_score " +
                        "FROM highscores " +
                        "GROUP BY username " +
                        "ORDER BY best_score DESC " +
                        "LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             //stringből sql statement-et generál
             PreparedStatement stmt = conn.prepareStatement(sql);
             //eredményeket tárol, rs.next() ugrál az inputStreamen amig be nem zuhan
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(
                        new HighScoreDisplayEntry(
                                rs.getString("username"),
                                rs.getInt("best_score")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
    public void showHighScores() {
        List<HighScoreDisplayEntry> scores = getTopHighScores();
        try {
            renderer.render(scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
