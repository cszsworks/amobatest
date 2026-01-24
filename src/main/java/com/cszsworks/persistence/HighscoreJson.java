package com.cszsworks.persistence;
import com.cszsworks.persistence.model.Highscore;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;

// JSON ALAK:     {
//            "score": 680,
//            "playerName": "zsolti",
//            "playedAt": "2026-01-20T13:07:48"
//            },

//csinál egy lokális jsont, hozzáadva az eddigiekhez a tartalmat
public class HighscoreJson {
    private static final String FILE_PATH = "highscores.json";

    public static void saveHighscore(Highscore highscore) {
        try {
            JSONArray array; //ez a modell tárolja az összeset

            File file = new File(FILE_PATH);
            if (file.exists()) {
                // Betölti az eddigi JSON tömböt
                //beolvassa a fájlt és utána tömböt gyárt belőle
                String content = new String(Files.readAllBytes(file.toPath()));
                array = new JSONArray(content);
            } else {
                // Új ha a fájl nem létezik még
                array = new JSONArray();
            }

            // Highscoreból - > JSON object
            JSONObject obj = new JSONObject();
            //obj.put key value párokat ad hozzá
            obj.put("playerName", highscore.getPlayerName());
            obj.put("score", highscore.getScore());
            //az időnél az SQL formátum eléréséhez igy jutunk. FONTOS H KOMPATIBILIS
            obj.put(
                    "playedAt",
                    highscore.getPlayedAt()
                            .withNano(0)
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );


            // JSON object -> JSON tömbbe
            array.put(obj);

            // Vissza a fájlba, törésekkel
            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                writer.write(array.toString(4));
            }

            System.out.println("Highscore saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
