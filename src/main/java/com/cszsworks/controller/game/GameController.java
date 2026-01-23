package com.cszsworks.controller.game;

import com.cszsworks.controller.menu.AppState;
import com.cszsworks.model.CellVO;
import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;
import com.cszsworks.persistence.HighscoreJson;
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
    private final GameConfig config; //playerName, board size, winLength, turn info
    private final LanternaGameRenderer renderer;
    public AppState appState = AppState.IN_GAME;
    public GameState state = GameState.PLAYING;
    //ez vezeti át az üzeneteket
    private String message = null;
    private int cursorRow = 0;
    private int cursorCol = 0;


    // Új játék konstruktora
    public GameController(Table table, GameConfig config, LanternaGameRenderer renderer) {
        this.table = table;
        this.config = config;
        this.renderer = renderer;

        //ez a kezdési logika lép életbe ha új játék. itt a konstruk berakja a játékos helyett az X-et
        if (config.isPlayerTurn()) {
            int centerRow = table.getRows() / 2;
            int centerCol = table.getCols() / 2;
            table.setCell(centerRow, centerCol, CellVO.Value.X);
            config.setPlayerTurn(false);
            cursorRow = centerRow;
            cursorCol = centerCol;
        }
    }

    // Load game konstruktora
    public GameController(GameSaveData gameSave, LanternaGameRenderer renderer) {
        this.table = gameSave.getTable();
        this.config = gameSave.getConfig();
        this.renderer = renderer;
    }


    // --- GAME LOOP ---

    public AppState gameLoop() throws Exception {
        while (state == GameState.PLAYING) {

            // full alap render
            renderer.renderGame(table, cursorRow, cursorCol, message);
            message = null; //üzenet nullázása, következő hivásnál már üres.

            // wincheck
            CellVO.Value winner = table.checkWinner();
            if (winner == CellVO.Value.X) {
                Highscore winnerScore = new Highscore(config.getPlayerName(), ScoreCalculator.CalculateScore(config));
                HighscoreJson.saveHighscore(winnerScore);
                state = GameState.X_WINS;
                message = "You won! Press Enter to return to the main menu";
            } else if (winner == CellVO.Value.O) {
                state = GameState.O_WINS;
                message = "AI won! Press Enter to return to the main menu";
            } else if (table.isBoardFull()) {
                state = GameState.DRAW;
                message = "Draw! Press Enter to return to the main menu";
            }

            if (state != GameState.PLAYING) {
                appState = AppState.MAIN_MENU;
                break;
            }

            // player / AI váltás
            if (config.isPlayerTurn()) {
                appState = handlePlayerInput();
                if (appState == AppState.MAIN_MENU) return appState;
            } else {
                if (!aiMove()) {
                    state = GameState.DRAW;
                    message = "Draw! Press Enter to return to the main menu";
                    appState = AppState.MAIN_MENU;
                    break;
                }
            }
        }

        //utolsó render a játékeredményre
        renderer.renderGame(table, cursorRow, cursorCol, message);
        WaitForKeyPress.waitForEnter(renderer);

        return appState;
    }

    // --- INPUT KEZELÉS ---

    private AppState handlePlayerInput() throws Exception {
        boolean turnDone = false;
        KeyStroke key = renderer.getScreen().readInput();
        if (key == null) return AppState.IN_GAME;

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
                if (state != GameState.PLAYING) {
                    appState = AppState.MAIN_MENU;
                } else if (movePossible(cursorRow, cursorCol)) {
                    table.setCell(cursorRow, cursorCol, CellVO.Value.X);
                    config.setPlayerTurn(false);
                    turnDone = true;
                } else {
                    message = "Cell is not available!";
                }
            }
            case Escape, F5 -> {
                saveGame();
                return AppState.MAIN_MENU;
            }
            default -> {}
        }

        if (turnDone) config.setPlayerTurn(false);
        return AppState.IN_GAME;
    }

    //lokális save-t hivja meg

    private void saveGame() {
        GameSaveData save = new GameSaveData(config, table);
        String filename = config.getPlayerName() + "_save.dat";
        SaveManager.createSave(save, filename);
        System.out.println("Game saved to " + filename);
    }
    //ai mozgás
    public boolean aiMove() {
        if (table.isBoardFull()) return false;
        //betölti összes lehetséges poziciót egy array-be
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < table.getRows(); i++) {
            for (int j = 0; j < table.getCols(); j++) {
                positions.add(new int[]{i, j});
            }
        }
        //összevekeri a lehetséges pozicókat, ettől lesz random a következő lépés
        Collections.shuffle(positions);

        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];
            if (movePossible(row, col)) {
                table.setCell(row, col, CellVO.Value.O);
                config.setPlayerTurn(true); // irányítás a playernél
                return true;
            }
        }
        return false;
    }

    public boolean movePossible(int row, int col) {

        //üres-e , valamit a csatlakozás követlmény igaz-e
        return table.getCell(row, col).value() == CellVO.Value.EMPTY
                && table.isConnectedToExisting(row, col);
    }



}
