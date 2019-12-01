package com.tema1.auxiliary;

import java.util.Comparator;

public final class ScoreComparator implements Comparator<ScoreBoardEntry> {
    public int compare(final ScoreBoardEntry c1, final ScoreBoardEntry c2) {
        return c2.getScore() - c1.getScore();
    }
}
