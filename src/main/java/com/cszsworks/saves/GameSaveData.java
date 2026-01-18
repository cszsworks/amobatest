package com.cszsworks.saves;

import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;

import java.io.Serializable;

public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final GameConfig config;
    private final Table table;
    private final boolean playerTurn;

    public GameSaveData(GameConfig config, Table table, boolean playerTurn) {
        this.config = config;
        this.table = table;
        this.playerTurn = playerTurn;
    }

    public GameConfig getConfig() { return config; }
    public Table getTable() { return table; }
    public boolean isPlayerTurn() { return playerTurn; }
}
