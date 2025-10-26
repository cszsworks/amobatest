package org.example;

public class Game {

    public static void main(String[] args) {
        System.out.println("Hello Amőba");
        Table table = new Table(3,4,5 );
        System.out.println(table.getCell(1,3));
        System.out.println(table.getCell(5,6));
    }
}
