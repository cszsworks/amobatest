package com.cszsworks;

import com.cszsworks.controller.game.GameController;
import com.cszsworks.controller.menu.AppState;
import com.cszsworks.controller.menu.MenuController;
import com.cszsworks.model.CellVO;
import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;
import com.cszsworks.saves.GameSaveData;
import com.cszsworks.saves.SaveManager;
import com.cszsworks.view.LanternaGameRenderer;
import com.cszsworks.view.LanternaMenuRenderer;
import com.cszsworks.view.StatusBarRenderer;
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
            playerName = "Player1"; // fallback default
        }

        System.out.println("Hello, " + playerName + "!");


        AppState appState = AppState.MAIN_MENU;
        int menuSelection = 0;

        //-----------------start Lanterna főképernyő--------------
        Screen mainScreen;

        // SwingTerminalFrame használata,legalább egy AutoCloseTrigger kell

        SwingTerminalFrame terminal = new SwingTerminalFrame(
                "Amőba",
                TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode
        );

        terminal.setVisible(true);
        terminal.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE); //program megáll háttérben ha az ablak zár

        // most a lanterna terminal körülveszi
        try {
            mainScreen = new TerminalScreen(terminal);
            mainScreen.startScreen();
            mainScreen.setCursorPosition(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LanternaGameRenderer gameRenderer;
        LanternaMenuRenderer menuRenderer;
        StatusBarRenderer statusBar;
        try {
            gameRenderer = new LanternaGameRenderer(mainScreen);
            menuRenderer = new LanternaMenuRenderer(mainScreen);
            statusBar = new StatusBarRenderer(mainScreen);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }








        while(appState != AppState.EXIT)
        {
            if(appState == AppState.MAIN_MENU)
            {
                MenuController controlMenu = new MenuController(menuSelection,menuRenderer);
                try {
                    appState = controlMenu.startMainMenu();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
            }
            }
            else if(appState == AppState.IN_GAME)
            {
                GameConfig newGame = new GameConfig(playerName,5,5,4);
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
