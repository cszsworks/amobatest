package com.cszsworks.controller.game;

import com.cszsworks.GameState;
import com.cszsworks.model.CellVO;
import com.cszsworks.model.Table;
import com.cszsworks.view.LanternaRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class GameController {

    private final Table table;
    private final LanternaRenderer renderer;
    private boolean playerTurn = true;
    private int cursorRow = 0;
    private int cursorCol = 0;

    public GameController(Table table, LanternaRenderer renderer) {
        this.table = table;
        this.renderer = renderer;
    }

    //segédmethod, enter inputra várás
    private void waitForEnter() throws Exception {
        while (true) {
            KeyStroke key = renderer.getScreen().readInput();
            if (key == null) continue;

            if (key.getKeyType() == KeyType.Enter) {
                return;
            }

            if (key.getKeyType() == KeyType.Escape) {
                renderer.close();
                System.exit(0);
            }
        }
    }


    public void gameLoop() throws Exception {
        GameState state = GameState.PLAYING;

        while (state == GameState.PLAYING) {

            renderer.renderGame(state, table, cursorRow, cursorCol);

            CellVO.Value winner = table.checkWinner();
            if (winner == CellVO.Value.X) {
                state = GameState.X_WINS;
            } else if (winner == CellVO.Value.O) {
                state = GameState.O_WINS;
            } else if (table.isBoardFull()) {
                state = GameState.DRAW;
            }

            if (state != GameState.PLAYING) {
                break;
            }

            if (playerTurn) {
                handlePlayerInput();
            } else {
                if (!aiMove()) {
                    state = GameState.DRAW;
                    break;
                }
            }
        }

        renderer.renderGame(state, table, cursorRow, cursorCol);

        waitForEnter();
        renderer.close();
    }

    private void handlePlayerInput() throws Exception {
        boolean turnDone = false;
        KeyStroke key = renderer.getScreen().readInput(); // blocking call
        if (key == null) return;

        KeyType type = key.getKeyType();
        //lanterna billentyü input, WASD és kurzormozgatóval is irányítható
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
