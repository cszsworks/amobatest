package com.cszsworks;

import com.cszsworks.controller.game.GameController;
import com.cszsworks.controller.menu.AppState;
import com.cszsworks.controller.menu.MenuController;
import com.cszsworks.model.Table;
import com.cszsworks.view.LanternaGameRenderer;
import com.cszsworks.view.LanternaMenuRenderer;
import com.cszsworks.view.StatusBarRenderer;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import java.io.IOException;

public class Game {



    public static void main(String[] args) {

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

        Table table = new Table(3, 3, 2);
        GameController controlGame = new GameController(table, gameRenderer);
        MenuController controlMenu = new MenuController(menuSelection,menuRenderer);



//        System.out.println("Hello Amőba");
//        Table table = new Table(3, 3, 2);
//        GameController testControl = new GameController(table, gameRenderer);
//
//        try {
//            controlGame.gameLoop();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        while(appState != AppState.EXIT)
        {
            if(appState == AppState.MAIN_MENU)
            {
                try {
                    appState = controlMenu.startMainMenu();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if(appState == AppState.IN_GAME)
            {
                try {
                    controlGame.gameLoop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }
        try{
            mainScreen.close();
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);




    }
}
