package com.cszsworks;

import com.cszsworks.model.CellVO;
import com.cszsworks.model.Table;
import com.cszsworks.view.LanternaRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {

    private final Table table;
    private final LanternaRenderer renderer;
    private boolean playerTurn = true;
    private int cursorRow = 0;
    private int cursorCol = 0;

    public Controller(Table table, LanternaRenderer renderer) {
        this.table = table;
        this.renderer = renderer;
    }


    public void gameLoop() throws Exception {
        while (true) {
            // Render the table with current cursor
            renderer.render(table, cursorRow, cursorCol);

            // Check winner
            CellVO.Value winner = table.checkWinner();
            if (winner != CellVO.Value.EMPTY) {
                System.out.println("Winner: " + winner);
                break;
            }

            // Check draw
            if (table.isBoardFull()) {
                System.out.println("Draw!");
                break;
            }

            if(playerTurn) handlePlayerInput();
            else{
                if(aiMove()==false) //csak akkor false a return ha tele a table
                {
                    System.out.println("Döntetlen!");
                    break;
                };
            }


        }

        // Cleanup screen after game ends
        renderer.close();
    }

    private void handlePlayerInput() throws Exception {
        boolean turnDone = false;
        KeyStroke key = renderer.getScreen().readInput(); // blocking call
        if (key == null) return;

        KeyType type = key.getKeyType();
        switch (type) {
            case ArrowUp -> cursorRow = Math.max(0, cursorRow - 1);
            case ArrowDown -> cursorRow = Math.min(table.getRows() - 1, cursorRow + 1);
            case ArrowLeft -> cursorCol = Math.max(0, cursorCol - 1);
            case ArrowRight -> cursorCol = Math.min(table.getCols() - 1, cursorCol + 1);
            case Character -> {
                char c = key.getCharacter();
                if (c == 'w') cursorRow = Math.max(0, cursorRow - 1);
                else if (c == 's') cursorRow = Math.min(table.getRows() - 1, cursorRow + 1);
                else if (c == 'a') cursorCol = Math.max(0, cursorCol - 1);
                else if (c == 'd') cursorCol = Math.min(table.getCols() - 1, cursorCol + 1);
            }
            case Enter ->
                    {
                        if(!movePossible(cursorRow, cursorCol)){  //ellenőrzöm hogy üres e a kívánt mező
                            System.out.println("A mező nem üres!");
                        }
                        else {
                            placePlayerMark();
                            turnDone = true;
                        }

                    }
            case Escape -> {
                renderer.close();
                System.exit(0);
            }
            default -> {}
        }
        playerTurn = !turnDone;

    }


    private void placePlayerMark() {
        if (movePossible(cursorRow, cursorCol)) {
            table.setCell(cursorRow, cursorCol, CellVO.Value.X);
        }
    }

    public boolean aiMove() {
        if (table.isBoardFull()) return false;

        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < table.getRows(); i++) {
            for (int j = 0; j < table.getCols(); j++) {
                positions.add(new int[]{i, j});
            }
        }

        Collections.shuffle(positions);

        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];
            if (movePossible(row, col)) {
                table.setCell(row, col, CellVO.Value.O);
                playerTurn = true; //váltás a játékos körére
                return true;
            }
        }
        return false;
    }

    public boolean movePossible(int row, int col) {
        return table.getCell(row, col).getValue() == CellVO.Value.EMPTY;
    }
}
