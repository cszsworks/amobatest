package com.cszsworks;

import com.cszsworks.model.Table;
import com.cszsworks.view.LanternaRenderer;

import javax.swing.*;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(8,4,10 );
        LanternaRenderer renderer = null;
        try
        {
            renderer = new LanternaRenderer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Controller testControl = new Controller(table,renderer);

        try
        {
            testControl.gameLoop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
