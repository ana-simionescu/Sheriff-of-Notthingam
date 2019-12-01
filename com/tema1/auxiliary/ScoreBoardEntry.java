package com.tema1.auxiliary;

public final class ScoreBoardEntry {
    private int id;
    private String playerType;
    private int score;
    public ScoreBoardEntry(final int id, final String playerType, final int score) {
        this.id = id;
        this.playerType = playerType;
        this.score = score;
    }
    int getScore() {
        return score;
    }
    public String toString() {
        return this.id + " " + this.playerType + " " + this.score;
    }
}
