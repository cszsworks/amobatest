package org.example;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(4,4,3 );
        table.printTable();
        System.out.println("modositom:");
        table.setCell(0,3,CellVO.Value.O);
        table.setCell(1,2,CellVO.Value.O);
        table.setCell(2,0,CellVO.Value.O);
        table.printTable();
        System.out.println("A nyertes jel:" + table.checkWinner());

    }
}
