package com.cszsworks.controller.menu;

import com.cszsworks.exception.InvalidInputException;
import com.cszsworks.model.GameConfig;
import com.cszsworks.view.LanternaNewGameRenderer;

public class NewGameMenuController {

    private final LanternaNewGameRenderer renderer;
    private GameConfig gameConfig;

    public NewGameMenuController(LanternaNewGameRenderer renderer) {
        this.renderer = renderer;
    }

    public void startNewMenu(String playerName) throws InvalidInputException {
        renderer.render();

        int size, win;

        try {
            size = Integer.parseInt(renderer.getSizeText());
            win  = Integer.parseInt(renderer.getWinLengthText());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Size and Win Length must be integers!");
        }

        // Méret korlátozása 4-25
        if (size < 4) size = 4;
        else if (size > 25) size = 25;

        // Win length korlátozása 2-25
        if (win < 2 || win > 25) win = 5;

        // gameConfig-be mentés
        gameConfig = new GameConfig(playerName, size, size, win);
    }
    public GameConfig getGameConfig() {
        return gameConfig;
    }
}
