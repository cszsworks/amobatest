package com.cszsworks.controller.game;

import com.cszsworks.controller.menu.AppState;
import com.cszsworks.model.CellVO;
import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;
import com.cszsworks.saves.GameSaveData;
import com.cszsworks.saves.SaveManager;
import com.cszsworks.view.LanternaGameRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    private final Table table;
    private final GameConfig config; // holds playerName, board size, winLength, turn info
    private final LanternaGameRenderer renderer;

    private int cursorRow = 0;
    private int cursorCol = 0;

    // --- CONSTRUCTORS ---

    // New game
    public GameController(Table table, GameConfig config, LanternaGameRenderer renderer) {
        this.table = table;
        this.config = config;
        this.renderer = renderer;
    }

    // Load game
    public GameController(GameSaveData gameSave, LanternaGameRenderer renderer) {
        this.table = gameSave.getTable();
        this.config = gameSave.getConfig();
        this.renderer = renderer;
    }

    public GameConfig getConfig() {
        return config;
    }

    // --- GAME LOOP ---

    public AppState gameLoop() throws Exception {
        AppState appState = AppState.IN_GAME;
        GameState state = GameState.PLAYING;

        while (state == GameState.PLAYING) {
            renderer.renderGame(state, table, cursorRow, cursorCol);

            CellVO.Value winner = table.checkWinner();
            if (winner == CellVO.Value.X) state = GameState.X_WINS;
            else if (winner == CellVO.Value.O) state = GameState.O_WINS;
            else if (table.isBoardFull()) state = GameState.DRAW;

            if (state != GameState.PLAYING) {
                appState = AppState.MAIN_MENU;
                break;
            }

            if (config.isPlayerTurn()) {
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
        return appState;
    }

    // --- INPUT HANDLING ---

    private void handlePlayerInput() throws Exception {
        boolean turnDone = false;
        KeyStroke key = renderer.getScreen().readInput();
        if (key == null) return;

        switch (key.getKeyType()) {
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
            case Enter -> {
                if (movePossible(cursorRow, cursorCol)) {
                    table.setCell(cursorRow, cursorCol, CellVO.Value.X);
                    config.setPlayerTurn(false); // switch turn
                    turnDone = true;
                } else {
                    System.out.println("A mező nem üres!");
                }
            }
            case Escape -> {
                renderer.close();
                System.exit(0);
            }
            case F5 -> saveGame();
            default -> {}
        }

        if (turnDone) config.setPlayerTurn(false);
    }

    // --- GAME LOGIC ---

    private void saveGame() {
        GameSaveData save = new GameSaveData(config, table, config.isPlayerTurn());
        String filename = config.getPlayerName() + "_save.dat";
        SaveManager.createSave(save, filename);
        System.out.println("Game saved to " + filename);
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
                config.setPlayerTurn(true); // switch back to player
                return true;
            }
        }
        return false;
    }

    public boolean movePossible(int row, int col) {
        return table.getCell(row, col).getValue() == CellVO.Value.EMPTY;
    }

    // --- UTILITY ---

    private void waitForEnter() throws Exception {
        while (true) {
            KeyStroke key = renderer.getScreen().readInput();
            if (key == null) continue;
            if (key.getKeyType() == KeyType.Enter || key.getKeyType() == KeyType.Escape) return;
        }
    }
}
