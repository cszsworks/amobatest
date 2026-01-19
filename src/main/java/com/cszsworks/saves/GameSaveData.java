package com.cszsworks.saves;

import com.cszsworks.model.GameConfig;
import com.cszsworks.model.Table;

import java.io.Serializable;

public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final GameConfig config;
    private final Table table;

    public GameSaveData(GameConfig config, Table table) {
        this.config = config;
        this.table = table;
    }

    public GameConfig getConfig() {
        return config;
    }

    public Table getTable() {
        return table;
    }
}