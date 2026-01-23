package com.cszsworks.persistence;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JSONToSQL {

    private static final String DB_URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7814323";
    private static final String DB_USER = "sql7814323";
    private static final String DB_PASS = "W3Ey4JZ2ae";

    public static void exportJSONToSQL(String jsonFilePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONArray array = new JSONArray(content);

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                //az ignore a duplicate kihagyás username,highscore,updated_at-re
                String sql = "INSERT IGNORE INTO highscores (username, highscore, updated_at) VALUES (?, ?, ?)";
                //előre megírt SQL query
                PreparedStatement stmt = conn.prepareStatement(sql);

                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    //begyüjti az adatokat a json keyektől
                    String playerName = obj.getString("playerName");
                    int score = obj.getInt("score");
                    LocalDateTime playedAt = LocalDateTime.parse(obj.getString("playedAt"), formatter)
                            .withNano(0);

                    //sorrendben pótoljuk a ?-eket
                    stmt.setString(1, playerName);
                    stmt.setInt(2, score);
                    stmt.setObject(3, playedAt);
                    stmt.executeUpdate();
                }
            }

            System.out.println("JSON imported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
