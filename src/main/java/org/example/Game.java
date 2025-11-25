package org.example;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(4,4,2 );
        table.setCell(0,0,CellVO.Value.X);
        table.printTable();
        System.out.println("modositom:");
        table.setCell(0,0,CellVO.Value.O);
        table.setCell(3,3,CellVO.Value.X);
        table.setCell(3,2,CellVO.Value.X);
        table.setCell(3,1,CellVO.Value.O);
        table.setCell(3,0,CellVO.Value.X);
        table.printTable();
        System.out.println(table.checkWinner());

    }
}
