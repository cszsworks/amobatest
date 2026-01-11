package com.cszsworks;

import com.cszsworks.controller.game.GameController;
import com.cszsworks.model.Table;
import com.cszsworks.view.LanternaRenderer;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(10,10,4 );
        LanternaRenderer renderer = null;
        try
        {
            renderer = new LanternaRenderer();
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
