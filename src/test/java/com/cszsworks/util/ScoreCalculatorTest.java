package com.cszsworks.util;

import com.cszsworks.model.GameConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreCalculatorTest {

    @Test
    public void testCalculateScore() {
        // Example: 5x5 tabla, 4es winhossz
        GameConfig config = new GameConfig("Player1", 5, 5, 4);

        int expectedScore = (5 * 5) * 10 + (4 * 10); // 250 + 40 = 290
        int actualScore = ScoreCalculator.CalculateScore(config);

        assertEquals(expectedScore, actualScore, "Score should match the expected calculation");
    }

    @Test
    public void testCalculateScoreMinimal() {
        // 4x4 tábla, 2es winhossz
        GameConfig config = new GameConfig("Player2", 4, 4, 2);

        int expectedScore = (4 * 4) * 10 + (2 * 10); // 160 + 20 = 180
        int actualScore = ScoreCalculator.CalculateScore(config);

        assertEquals(expectedScore, actualScore, "Score should match for minimal board");
    }

    @Test
    public void testCalculateScoreMaximal() {
        // 25x25 tábla, 25ös winhossz
        GameConfig config = new GameConfig("Player3", 25, 25, 25);

        int expectedScore = (25 * 25) * 10 + (25 * 10); // 6250 + 250 = 6500
        int actualScore = ScoreCalculator.CalculateScore(config);

        assertEquals(expectedScore, actualScore, "Score should match for maximal board");
    }
}
