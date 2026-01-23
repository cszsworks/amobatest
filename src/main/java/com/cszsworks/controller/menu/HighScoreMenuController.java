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

    //top 10 highscore usernév szerint
    public List<HighScoreDisplayEntry> getTopHighScores() {
        List<HighScoreDisplayEntry> results = new ArrayList<>();
        //előre megírt SQL query
        String sql = "SELECT username, MAX(highscore) AS best_score " +
                "FROM highscores " +
                "GROUP BY username " +
                "ORDER BY best_score DESC " +
                "LIMIT 10";

        //létrehoz egy objektumot, a rs.next pedig pusholja rajta a "kurzort", amig létezik, megy
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new HighScoreDisplayEntry(
                        //a highscoredisplayentry modelhez adja a megfelelő fieldeket
                        rs.getString("username"),
                        rs.getInt("best_score")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    //meghivja a hs renderert
    public void showHighScores() {
        List<HighScoreDisplayEntry> scores = getTopHighScores();
        renderer.render(scores); // now includes a close button
        //gui2 visszaadja a controlt zárás után
    }

}
