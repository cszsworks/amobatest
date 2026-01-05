package com.cszsworks;

import com.cszsworks.model.CellVO;
import com.cszsworks.model.Table;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(2,2,3 );
        table.printTable();
        System.out.println("modositom:");
        table.setCell(1,1, CellVO.Value.O);
        table.setCell(1,0,CellVO.Value.O);
        table.setCell(0,0, CellVO.Value.O);
        table.setCell(0,1,CellVO.Value.O);
        table.printTable();
        if(table.isBoardFull()){
            System.out.println("Board full,game drawn");

        }
        else
        {
            System.out.println("A nyertes jel:" + table.checkWinner());
        }


    }
}
