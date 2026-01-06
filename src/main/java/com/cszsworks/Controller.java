package com.cszsworks;


import com.cszsworks.model.CellVO;
import com.cszsworks.model.Table;
import com.cszsworks.view.Renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    private final Table table;
    private boolean playerTurn;
    private final Renderer renderer;

    public Controller(Table table, Renderer renderer)
    {
       this.table = table;
       this.playerTurn = false;
       this.renderer = renderer;
    }

    //összes lehetséges lepes arrayList, ezt shuffle, majd rendezetten vegignezve a listat
    public boolean aiMove ()
    {
        if(this.table.isBoardFull())
        {
            return false;
        }
        List<int[]> positions = new ArrayList<>();

        // collect all board positions
        for (int i = 0; i < table.getRows(); i++) {
            for (int j = 0; j < table.getCols(); j++) {
                positions.add(new int[]{i, j});
            }
        }

        Collections.shuffle(positions);

        //egyszer proba mindent
        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];

            if (movePossible(row, col)) {
                table.setCell(row, col, CellVO.Value.O);
                return true;
            }
        }

        return false;
    }

    public void callTableDraw()
    {
        this.renderer.render(this.table);
    }
    //lehetséges e lépés az adott fielden
    public boolean movePossible(int row, int col)
    {
        CellVO move = this.table.getCell(row,col);
        CellVO.Value value = move.getValue();

        return value == CellVO.Value.EMPTY;
    }
}
