package com.cszsworks.util;

import com.cszsworks.model.GameConfig;

public class ScoreCalculator {

    public static int CalculateScore(GameConfig config)
    {
        int score = (config.getRows() * config.getCols())*10 + (config.getWinLength()*10);
        return score;
    }
}
