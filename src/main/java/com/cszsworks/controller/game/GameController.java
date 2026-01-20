package com.cszsworks.controller.game;

import com.cszsworks.controller.menu.AppState;
import com.cszsworks.model.CellVO;
import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;
import com.cszsworks.persistence.json.HighscoreJson;
import com.cszsworks.persistence.model.Highscore;
import com.cszsworks.saves.GameSaveData;
import com.cszsworks.saves.SaveManager;
import com.cszsworks.util.ScoreCalculator;
import com.cszsworks.util.WaitForKeyPress;
import com.cszsworks.view.LanternaGameRenderer;
import com.googlecode.lanterna.input.KeyStroke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    private final Table table;
    private final GameConfig config; // holds playerName, board size, winLength, turn info
    private final LanternaGameRenderer renderer;
    public AppState appState = AppState.IN_GAME;
    public GameState state = GameState.PLAYING;

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


        while (state == GameState.PLAYING) {
            renderer.renderGame(state, table, cursorRow, cursorCol);

            CellVO.Value winner = table.checkWinner();

            //HA AZ EMBER NYERT, HIGH SCORE-T GENERÁLUNK
            if (winner == CellVO.Value.X) {
                Highscore winnerScore = new Highscore(config.getPlayerName(), ScoreCalculator.CalculateScore(config));
                HighscoreJson.saveHighscore(winnerScore);
                state = GameState.X_WINS;
            }

            else if (winner == CellVO.Value.O) state = GameState.O_WINS;
            else if (table.isBoardFull()) state = GameState.DRAW;

            if (state != GameState.PLAYING) {
                appState = AppState.MAIN_MENU;
                break;
            }

            if (config.isPlayerTurn()) {
                appState = handlePlayerInput();
                if(appState == AppState.MAIN_MENU)return appState;
            } else {
                if (!aiMove()) {
                    state = GameState.DRAW;
                    break;
                }
            }
        }
        //itt hívom meg a renderer-t aszerint hogy milyen state-ben van a játék
        renderer.renderGame(state, table, cursorRow, cursorCol);
        WaitForKeyPress.waitForEnter(renderer);
        return appState;
    }

    // --- INPUT KEZELÉS ---

    private AppState handlePlayerInput() throws Exception {
        boolean turnDone = false;
        KeyStroke key = renderer.getScreen().readInput();
        if (key == null)  return AppState.IN_GAME;

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
                saveGame();
                return AppState.MAIN_MENU;
            }
            case F5 -> {
                saveGame();
                return appState.MAIN_MENU;
            }
            default -> {}
        }

        if (turnDone) config.setPlayerTurn(false);
        return AppState.IN_GAME;
    }

    // --- GAME LOGIC ---

    private void saveGame() {
        GameSaveData save = new GameSaveData(config, table);
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


}
