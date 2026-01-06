package com.cszsworks;

import com.cszsworks.model.CellVO;
import com.cszsworks.model.Table;
import com.cszsworks.Controller;
import com.cszsworks.view.Renderer;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(10,10,2 );
        table.setCell(1,1, CellVO.Value.X);
        table.setCell(1,0,CellVO.Value.X);
        table.setCell(0,0, CellVO.Value.X);
        table.setCell(0,1,CellVO.Value.X);
        Renderer renderer = new Renderer();
        Controller testControl = new Controller(table,renderer);

        testControl.callTableDraw();
        testControl.aiMove();
        testControl.aiMove();
        testControl.aiMove();
        testControl.aiMove();
        testControl.callTableDraw();
        testControl.aiMove();
        testControl.aiMove();
        testControl.aiMove();
        testControl.callTableDraw();

    }
}
