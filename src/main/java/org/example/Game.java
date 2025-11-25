package org.example;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(3,4,5 );
        table.setCell(0,0,CellVO.Value.X);
        table.printTable();
        System.out.println("modositom:");
        table.setCell(0,0,CellVO.Value.X);
        table.setCell(0,0,CellVO.Value.O);
        table.setCell(2,2,CellVO.Value.X);
        table.setCell(5,5,CellVO.Value.X);
        table.printTable();
    }
}
