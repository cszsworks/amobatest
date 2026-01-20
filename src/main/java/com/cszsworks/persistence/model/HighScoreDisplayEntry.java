package com.cszsworks.persistence.model;

public class HighScoreDisplayEntry {
    private final String username;
    private final int score;

    public HighScoreDisplayEntry(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}
