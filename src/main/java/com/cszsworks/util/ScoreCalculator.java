package com.cszsworks.util;

import com.cszsworks.model.GameConfig;
//egy hasraütés score kalkuláció a pálya mérete és a nyerési hossz alapján
public class ScoreCalculator {

    public static int CalculateScore(GameConfig config)
    {
        return (config.getRows() * config.getCols())*10 + (config.getWinLength()*10);

    }
}
