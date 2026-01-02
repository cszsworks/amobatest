package org.example;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Controller {
    private final Table table;
    private boolean playerTurn;

    public Controller(Table table)
    {
       this.table = table;
       this.playerTurn = false;
    }

    public boolean aiMove ()
    {
        return true;
    }

    //lehetséges e lépés az adott fielden
    public boolean movePossible(int row, int col)
    {
        CellVO move = this.table.getCell(row,col);
        CellVO.Value value = move.getValue();

        return value == CellVO.Value.EMPTY;
    }
}
