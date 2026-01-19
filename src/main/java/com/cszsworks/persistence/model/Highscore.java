package com.cszsworks.persistence.model;

import java.time.LocalDateTime;

public class Highscore {

    private String playerName;
    private int score;
    private LocalDateTime playedAt;

    public Highscore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.playedAt = LocalDateTime.now();
    }

    // getters
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public LocalDateTime getPlayedAt() { return playedAt; }
}
