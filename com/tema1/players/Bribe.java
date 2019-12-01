package com.tema1.players;

import java.util.ArrayList;

public final class Bribe extends BasePlayer {

    public Bribe(final ArrayList<Integer> assets) {
        super(assets);
        setbasic();
        setgreedy(false);
        setbribe(true);
    }
}
