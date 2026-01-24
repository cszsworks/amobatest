package com.cszsworks.persistence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;

//PreparedStatement használat JDBCből
public class SQLToJSON {
    //adatbázis kapcsolat adatok
    private static final String DB_URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7814323";
    private static final String DB_USER = "sql7814323";
    private static final String DB_PASS = "W3Ey4JZ2ae";

    public static boolean exportSQLToJSON(String jsonFilePath) {
        JSONArray array = new JSONArray();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            //preparedstatement lekrédezés futtatása
            String sql = "SELECT username, highscore, updated_at FROM highscores";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            //beolvasó "kurzor" mozog a tartalomban
            while (rs.next()) {
                //minden ugrás egy objektum, egészen a végéig
                JSONObject obj = new JSONObject();
                //json objektum megtöltése a mezők visszaolvasása szeirnt
                obj.put("playerName", rs.getString("username"));
                obj.put("score", rs.getInt("highscore"));
                obj.put(
                        "playedAt",
                        rs.getTimestamp("updated_at").toLocalDateTime().format(formatter)
                );

                array.put(obj);
            }
            //visszaírja json fájlba a tartalmat
            Files.write(
                    Paths.get(jsonFilePath),
                    array.toString(4).getBytes()
            );

            System.out.println("SQL exported to JSON successfully!");
            return true;

        } catch (Exception e) {
            System.err.println("Database unavailable, using local JSON fallback.");
            return false;
        }
    }


}
