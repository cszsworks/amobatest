package com.cszsworks.model;

import java.io.Serial;
import java.io.Serializable;

public class GameConfig implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String playerName;
    private final int rows;
    private final int cols;
    private final int winLength;
    private boolean playerTurn; // true = player X's turn, false = AI/O's turn

    public GameConfig(String playerName, int rows, int cols, int winLength) {
        this.playerName = playerName;
        this.rows = rows;
        this.cols = cols;
        this.winLength = winLength;
        this.playerTurn = true; // default: player starts
    }

    // --- GETTERS ---
    public String getPlayerName() {
        return playerName;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getWinLength() {
        return winLength;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    // --- SETTERS ---
    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }
}
