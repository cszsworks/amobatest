package com.cszsworks.controller.menu;

import com.cszsworks.model.GameConfig;
import com.cszsworks.view.LanternaNewGameRenderer;

public class NewGameMenuController {

    private final LanternaNewGameRenderer renderer;
    private GameConfig gameConfig;

    public NewGameMenuController(LanternaNewGameRenderer renderer) {
        this.renderer = renderer;
    }

    public void startNewMenu(String playerName) {
        renderer.render();
        //amit a renderer visszaad a gui2 boxokból, intet csinál belőle
        int size = Integer.parseInt(renderer.getSizeText());
        int win  = Integer.parseInt(renderer.getWinLengthText());
        if(size < 4)size = 4;
        else if(size > 25)size = 25;
        //méret nem lehet túl nagy/kicsi, visszatolja
        if(win<2 || win > 25) win = 5;
        //winszám sztenderd 5 , ha inkorrekt
        //a gameconfig konténerbe menit az adatokat
        gameConfig = new GameConfig(playerName, size, size, win);
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }
}
