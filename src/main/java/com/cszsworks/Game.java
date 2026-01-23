package com.cszsworks;

import com.cszsworks.controller.game.GameController;
import com.cszsworks.controller.menu.AppState;
import com.cszsworks.controller.menu.HighScoreMenuController;
import com.cszsworks.controller.menu.MainMenuController;
import com.cszsworks.controller.menu.NewGameMenuController;
import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;
import com.cszsworks.persistence.JSONToSQL;
import com.cszsworks.persistence.SQLToJSON;
import com.cszsworks.saves.GameSaveData;
import com.cszsworks.saves.SaveManager;
import com.cszsworks.util.ThemeManager;
import com.cszsworks.view.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import java.io.IOException;
import java.util.Scanner;

public class Game {



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine().trim();

        if (playerName.isEmpty()) {
            playerName = "Player1"; // default
        }

        System.out.println("\nAvailable themes:");
        ThemeManager.getAvailableThemes().forEach(name -> System.out.println(" - " + name));

        System.out.print("Choose a theme (press Enter for default): ");
        String themeChoice = scanner.nextLine().trim();

        if (themeChoice.isEmpty() || !ThemeManager.setTheme(themeChoice)) {
            System.out.println("Using default theme.\n");
        } else {
            System.out.println("Theme set to " + themeChoice + "\n");
        }

        System.out.println("Hello, " + playerName + "!");
        //adatbazis beszélgetés

        String FILE_PATH = "highscores.json";
        //ha a process zar, a local data hozzáadódik az SQL-hez
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Uploading remaining local highscores to database...");
            JSONToSQL.exportJSONToSQL(FILE_PATH);
            System.out.println("Highscores upload finished.");
        }));

        SQLToJSON.exportSQLToJSON(FILE_PATH);

        AppState appState = AppState.MAIN_MENU;
        int menuSelection = 0;

        //-----------------start Lanterna főképernyő--------------
        Screen mainScreen;

        // SwingTerminalFrame használata,legalább egy AutoCloseTrigger kell

        SwingTerminalFrame terminal = new SwingTerminalFrame("Amőba",
                TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode
        );

        terminal.setVisible(true);
        terminal.toFront();       // Előtérbe ugrik
        terminal.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE); //program megáll háttérben ha az ablak zár

        // most a lanterna terminal körülveszi
        try {
            mainScreen = new TerminalScreen(terminal);
            mainScreen.startScreen();
            mainScreen.setCursorPosition(null);
            //HD méretü képernyö méretezés
            terminal.setSize(1366, 768);
            //a képernyő közepéhez mérten helyezi el az ablakot
            terminal.setLocationRelativeTo(null);
            //manualis resize hívás
            mainScreen.doResizeIfNecessary();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //létrehozom a renderer objektumokat
        LanternaGameRenderer gameRenderer;
        LanternaHighScoreRenderer highScoreRenderer;
        LanternaNewGameRenderer newGameRenderer;
        LanternaMenuRenderer menuRenderer;


        try {
            //betöltöm a renderer objektumokat
            gameRenderer = new LanternaGameRenderer(mainScreen);
            menuRenderer = new LanternaMenuRenderer(mainScreen);
            highScoreRenderer = new LanternaHighScoreRenderer(mainScreen);
            newGameRenderer =new LanternaNewGameRenderer(mainScreen);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }








        while(appState != AppState.EXIT)
        {
            if(appState == AppState.MAIN_MENU)
            {
                MainMenuController controlMenu = new MainMenuController(menuSelection,menuRenderer);
                try {
                    appState = controlMenu.startMainMenu();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            else if(appState == AppState.HIGH_SCORE_SCREEN) {
                // helyi JSON-ba tölti a távoli SQL-t ha van kapcsolat, és azt irja ki
                JSONToSQL.exportJSONToSQL(FILE_PATH);

                HighScoreMenuController highScoreControl = new HighScoreMenuController(highScoreRenderer);
                highScoreControl.showHighScores();

                appState = AppState.MAIN_MENU;
                //gui2 visszaadja főmenünek
            }

            else if(appState == AppState.LOAD_GAME)
            {
                GameSaveData loadedSave = SaveManager.loadSave(playerName + "_save.dat");
                if(loadedSave!=null) {
                    GameController controlGame = new GameController(loadedSave, gameRenderer);
                    try {
                        System.out.println("most loadolva");
                        appState = controlGame.gameLoop();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                    else {
                    System.out.println("No save file found!");
                    appState = AppState.MAIN_MENU;
                    //visszatér menübe
            }
            }
            else if(appState == AppState.NEW_GAME_SETUP)
            {
                NewGameMenuController newMenu = new NewGameMenuController(newGameRenderer);

                newMenu.startNewMenu(playerName);

                GameConfig newGame = newMenu.getGameConfig();
                Table newTable = new Table(
                        newGame.getRows(),
                        newGame.getCols(),
                        newGame.getWinLength()
                );

                GameController controlGame =
                        new GameController(newTable, newGame, gameRenderer);

                try{
                    appState = controlGame.gameLoop();
                }catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if(appState == AppState.IN_GAME)
            {
                GameConfig newGame = new GameConfig(playerName,8,8,4);
                Table newTable = new Table(newGame.getRows(), newGame.getCols(), newGame.getWinLength());

                GameController controlGame = new GameController(newTable,newGame,gameRenderer);
                try {
                    System.out.println("most ingame");
                    appState = controlGame.gameLoop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("menu egyszer lefut");

        }

        System.out.println("loop elhagyva");
        try{
            mainScreen.close();
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);




    }
}
