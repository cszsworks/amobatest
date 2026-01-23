package com.cszsworks.controller.menu;

import com.cszsworks.view.LanternaMenuRenderer;

public class MainMenuController {

    private final LanternaMenuRenderer renderer;
    private AppState appState;

    private final String[] options = {
            "New Game",
            "Load Game",
            "High Scores",
            "Quit"
    };

    private int menuSelection;

    public MainMenuController(int initialSelection, LanternaMenuRenderer renderer) {
        this.menuSelection = initialSelection;
        this.renderer = renderer;
        this.appState = AppState.MAIN_MENU;
    }

    public AppState startMainMenu() {

        // renderer automatikusan blokkolva adja vissza
        menuSelection = renderer.renderMenu("Main Menu", options, menuSelection);

        // a kivalasztott menut appstate-re forditja
        switch (menuSelection) {
            case 0 -> appState = AppState.NEW_GAME_SETUP;
            case 1 -> appState = AppState.LOAD_GAME;
            case 2 -> appState = AppState.HIGH_SCORE_SCREEN;
            case 3 -> appState = AppState.EXIT;
            default -> appState = AppState.MAIN_MENU;
        }

        return appState;
    }
}
