package com.cszsworks.controller.menu;

import com.cszsworks.view.LanternaMenuRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

public class MainMenuController {
    private final LanternaMenuRenderer renderer;
    public int menuSelection;
    private AppState appState;

    private String[] options = {"New Game", "Load Game", "High Scores", "Quit"};

    public MainMenuController(int menuSelection, LanternaMenuRenderer renderer) {
        this.menuSelection = menuSelection;
        this.renderer = renderer;
        appState = AppState.MAIN_MENU;
    }

    private void incrementMenu()
    {
        int limit = options.length;
        if(menuSelection < limit-1)
        {
            menuSelection++;
        }
    }

    private void decrementMenu()
    {
        if(menuSelection >=1)
        {
            menuSelection--;
        }
    }

    public AppState startMainMenu()
    {
        while(appState==AppState.MAIN_MENU)
        {
            try{
                renderer.renderMenu(options,menuSelection);
                handlePlayerInput();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return appState;
    }

    private void handlePlayerInput() throws Exception
    {

        KeyStroke key = renderer.getScreen().readInput();
        if (key == null) return;
        KeyType type = key.getKeyType();

        switch (type) {
            case ArrowUp -> decrementMenu();
            case ArrowDown -> incrementMenu();
            case Enter ->
            {
                switch (menuSelection)
                {
                    case 0 -> appState = AppState.NEW_GAME_SETUP;
                    case 1 -> appState = AppState.LOAD_GAME;
                    case 2 -> appState = AppState.HIGH_SCORE_SCREEN;
                    case 3 -> appState = AppState.EXIT;
                }
            }
            case Escape -> {
                renderer.close();
                System.exit(0);
            }


            default -> {}
        }
    }








}
