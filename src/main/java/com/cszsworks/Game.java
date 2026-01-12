package com.cszsworks;

import com.cszsworks.controller.game.GameController;
import com.cszsworks.model.Table;
import com.cszsworks.view.LanternaGameRenderer;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(3,3,2 );
        LanternaGameRenderer renderer = null;
        try
        {
            renderer = new LanternaGameRenderer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        GameController testControl = new GameController(table,renderer);

        try
        {
            testControl.gameLoop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
