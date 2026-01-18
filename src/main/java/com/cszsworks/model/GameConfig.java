package com.cszsworks.model;

import com.cszsworks.controller.game.GameState;

import java.io.Serializable;

public class GameConfig implements Serializable {
    //bytestreamként menthető, uid gen
    private static final long serialVersionUID = 1L;

    private final String playerName;
    private final int rows;
    private final int cols;
    private final int winLength;
    private int playerTurn;


    public GameConfig(String playerName, int rows, int cols, int winLength) {
        this.playerName = playerName;
        this.rows = rows;
        this.cols = cols;
        this.winLength = winLength;
    }

}
