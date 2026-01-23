package com.cszsworks;

import com.cszsworks.controller.game.GameController;
import com.cszsworks.controller.menu.*;
import com.cszsworks.exception.InvalidInputException;
import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;
import com.cszsworks.persistence.JSONToSQL;
import com.cszsworks.persistence.SQLToJSON;
import com.cszsworks.saves.GameSaveData;
import com.cszsworks.saves.SaveManager;
import com.cszsworks.view.*;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class Game {

    // --- korábban lokális változók voltak ---
    private final String playerName;
    private final Screen mainScreen;

    private final LanternaGameRenderer gameRenderer;
    private final LanternaMenuRenderer menuRenderer;
    private final LanternaHighScoreRenderer highScoreRenderer;
    private final LanternaNewGameRenderer newGameRenderer;

    public Game(
            String playerName,
            Screen mainScreen,
            LanternaGameRenderer gameRenderer,
            LanternaMenuRenderer menuRenderer,
            LanternaHighScoreRenderer highScoreRenderer,
            LanternaNewGameRenderer newGameRenderer
    ) {
        this.playerName = playerName;
        this.mainScreen = mainScreen;
        this.gameRenderer = gameRenderer;
        this.menuRenderer = menuRenderer;
        this.highScoreRenderer = highScoreRenderer;
        this.newGameRenderer = newGameRenderer;
    }

    // ------------------ FŐ JÁTÉK FOLYAM ------------------
    public void run() {
        System.out.println("Game.run() started!");

        String FILE_PATH = "highscores.json";

        // shutdown hook – kilépéskor mentjük az adatbázist
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Uploading remaining local highscores to database...");
            JSONToSQL.exportJSONToSQL(FILE_PATH);
            System.out.println("Highscores upload finished.");
        }));

        // induláskor DB → JSON
        SQLToJSON.exportSQLToJSON(FILE_PATH);

        AppState appState = AppState.MAIN_MENU;
        int menuSelection = 0;

        // fő ciklus – addig fut, míg EXIT nem lesz
        while (appState != AppState.EXIT) {

            if (appState == AppState.MAIN_MENU) {
                // főmenü
                MainMenuController controlMenu =
                        new MainMenuController(menuSelection, menuRenderer);
                try {
                    appState = controlMenu.startMainMenu();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else if (appState == AppState.HIGH_SCORE_SCREEN) {
                // high score képernyő
                JSONToSQL.exportJSONToSQL(FILE_PATH);
                HighScoreMenuController highScoreControl =
                        new HighScoreMenuController(highScoreRenderer);
                highScoreControl.showHighScores();
                appState = AppState.MAIN_MENU;

            } else if (appState == AppState.LOAD_GAME) {
                // mentett játék betöltése
                GameSaveData loadedSave =
                        SaveManager.loadSave(playerName + "_save.dat");

                if (loadedSave != null) {
                    GameController controlGame =
                            new GameController(loadedSave, gameRenderer);
                    try {
                        System.out.println("most loadolva");
                        appState = controlGame.gameLoop();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("No save file found!");
                    appState = AppState.MAIN_MENU;
                }

            } else if (appState == AppState.NEW_GAME_SETUP) {
                NewGameMenuController newMenu = new NewGameMenuController(newGameRenderer);
                //tesztelem hogy számokat kapott-e a textbox
                boolean validInput = false;
                while (!validInput) {
                    try {
                        // próbáljuk kiolvasni a felhasználó inputját
                        newMenu.startNewMenu(playerName);
                        validInput = true; // sikerült, kilépünk a while-ból
                    } catch (InvalidInputException e) {
                        System.out.println("Invalid input, numbers only");
                    }
                }

                // ha ide eljutottunk, akkor van érvényes GameConfig
                GameConfig newGame = newMenu.getGameConfig();

                Table newTable = new Table(
                        newGame.getRows(),
                        newGame.getCols(),
                        newGame.getWinLength()
                );

                GameController controlGame = new GameController(newTable, newGame, gameRenderer);

                try {
                    appState = controlGame.gameLoop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }}


            // debug jelzés – ciklus lefutott
            System.out.println("menu egyszer lefut");
        }

        System.out.println("loop elhagyva");

        try {
            mainScreen.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
