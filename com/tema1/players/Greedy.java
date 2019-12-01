package com.tema1.players;

import java.util.ArrayList;

public final class Greedy extends BasePlayer {

    public Greedy(final ArrayList<Integer> assets) {
        super(assets);
        setbasic();
        setgreedy(true);
        setbribe(false);
    }
}
