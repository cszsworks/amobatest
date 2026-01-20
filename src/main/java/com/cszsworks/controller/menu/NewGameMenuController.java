package com.cszsworks.controller.menu;

import com.cszsworks.model.GameConfig;
import com.cszsworks.view.LanternaNewGameRenderer;

public class NewGameMenuController {

    private final LanternaNewGameRenderer renderer;
    private GameConfig gameConfig;

    public NewGameMenuController(LanternaNewGameRenderer renderer) {
        this.renderer = renderer;
    }

    public AppState startNewMenu(String playerName) {
        renderer.render();

        int rows = Integer.parseInt(renderer.getRowsText());
        int cols = Integer.parseInt(renderer.getColsText());
        int win  = Integer.parseInt(renderer.getWinLengthText());

        gameConfig = new GameConfig(playerName, rows, cols, win);

        return AppState.IN_GAME;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }
}
