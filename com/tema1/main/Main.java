package com.tema1.main;

import com.tema1.auxiliary.ScoreBoardEntry;
import com.tema1.auxiliary.ScoreComparator;
import com.tema1.common.Constants;
import com.tema1.goods.GoodsFactory;
import com.tema1.players.BasePlayer;
import com.tema1.players.Bribe;
import com.tema1.players.Greedy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        //TODO implement homework logic
        ArrayList<BasePlayer> players = new ArrayList<>();
        ArrayList<Integer> assets = new ArrayList<>(gameInput.getAssetIds());
        // Create each player and add to the list
        for (int i = 0; i < gameInput.getPlayerNames().size(); i++) {
            if (gameInput.getPlayerNames().get(i).equals("basic")) {
                players.add(new BasePlayer(assets));
            }
            if (gameInput.getPlayerNames().get(i).equals("greedy")) {
                players.add(new Greedy(assets));
            }
            if (gameInput.getPlayerNames().get(i).equals("bribed")) {
                players.add(new Bribe(assets));
            }
        }
        // The logic of the game
        for (int i = 0; i < gameInput.getRounds(); i++) {
            // Each round has as many sub-rounds as players
            for (int j = 0; j < players.size(); j++) {
                for (int k = 0; k < players.size(); k++) {
                    if (j != k) {
                        players.get(k).drawCards();
                        players.get(k).createBag();
                    }
                }
                players.get(j).beSheriff(j, players);
            }
            for (BasePlayer player : players) {
                if (player.getgreedy()) {
                    player.incRoundNumber();
                }
            }
        }
        GoodsFactory inventory = new GoodsFactory();
        for (BasePlayer player : players) {
            player.addIllegalBonuses();
            player.addProfit();
        }
        // For each product I look for the two players
        // with the biggest frequency
        for (int i = 0; i < Constants.NO_OF_LEGAL_GOODS; i++) {
            int queen = 0, queenplayer = -1, king = 0, kingplayer = -1;
            for (int j = 0; j < players.size(); j++) {
                int noOfGoods = players.get(j).getNoOfGoodsById(i);
                if (noOfGoods > king) {
                    queen = king;
                    queenplayer = kingplayer;
                    king = noOfGoods;
                    kingplayer = j;
                } else {
                    if (noOfGoods > queen) {
                        queen = noOfGoods;
                        queenplayer = j;
                    }
                }
            }
            if (kingplayer != -1) {
                players.get(kingplayer).addMoney(inventory.getKingBonus(i));
            }
            if (queenplayer != -1) {
                players.get(queenplayer).addMoney(inventory.getQueenBonus(i));
            }
        }
        // Create the scoreboard
        List<ScoreBoardEntry> scoreBoard = new LinkedList<>();
        for (int i = 0; i < players.size(); i++) {
            String type = "";
            if (players.get(i).getbasic()) {
                type = "BASIC";
            }
            if (players.get(i).getgreedy()) {
                type = "GREEDY";
            }
            if (players.get(i).getbribe()) {
                type = "BRIBED";
            }
            scoreBoard.add(new ScoreBoardEntry(i, type, players.get(i).getMoney()));
        }
        scoreBoard.sort(new ScoreComparator());
        for (ScoreBoardEntry scoreBoardEntry : scoreBoard) {
            System.out.println(scoreBoardEntry);
        }
    }
}
