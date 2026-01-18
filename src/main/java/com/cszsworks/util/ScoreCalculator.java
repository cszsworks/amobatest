package com.cszsworks.util;

public class ScoreCalculator {

    public static int CalculateScore(int rows, int cols, int winLength)
    {
        int score = (rows * cols)*10 + (winLength*10);
        return score;
    }
}
