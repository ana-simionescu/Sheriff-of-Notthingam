package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.GoodsFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasePlayer {
    private boolean basic, greedy, bribe;
    private int roundNumber;
    private int givenBribe;
    private int money;
    private List<Integer> hand;
    private List<Integer> stand;
    private List<Integer> bag;
    private List<Integer> assets;
    private GoodsFactory inventory = new GoodsFactory();

    public BasePlayer(final ArrayList<Integer> assets) {
        hand = new ArrayList<>();
        stand = new ArrayList<>();
        bag = new ArrayList<>();
        money = Constants.START_MONEY;
        basic = true;
        greedy = false;
        bribe = false;
        this.assets = assets;
        roundNumber = 1;
    }

    public final boolean getbasic() {
        return basic;
    }

    public final boolean getgreedy() {
        return greedy;
    }

    public final boolean getbribe() {
        return bribe;
    }

    final void setbasic() {
        basic = false;
    }

    final void setgreedy(final boolean ok) {
        greedy = ok;
    }

    final void setbribe(final boolean ok) {
        bribe = ok;
    }

    private int getRoundNumber() {
        return roundNumber;
    }

    public final void incRoundNumber() {
        roundNumber++;
    }

    private void setGivenBribe(final int coins) {
        givenBribe = coins;
    }

    public final int getMoney() {
        return money;
    }

    public final void addMoney(final int coins) {
        money += coins;
    }

    private int getGivenBribe() {
        return givenBribe;
    }

    private List<Integer> getHand() {
        return hand;
    }

    private List<Integer> getBag() {
        return bag;
    }

    private GoodsFactory getInventory() {
        return inventory;
    }

    // Take 10 cards from deck and add to hand
    // Remove each card drawn from the deck
    public final void drawCards() {
        hand.clear();
        for (int i = 0; i < Constants.NO_OF_CARDS_IN_HAND; i++) {
            hand.add(assets.get(0));
            assets.remove(0);
        }
    }

    // The basic player's method for creating the bag
    private void makeBag() {
        int nr;
        bag.clear();
        int maxim = -1;
        Integer chosenID = -1;
        // Find the most frequent legal card
        for (int i = 0; i < hand.size(); i++) {
            nr = Collections.frequency(hand, hand.get(i));
            if (nr == maxim && hand.get(i) < Constants.NO_OF_LEGAL_GOODS
                    && inventory.getGoodsById(chosenID).getProfit()
                    < inventory.getGoodsById(hand.get(i)).getProfit()) {
                chosenID = hand.get(i);
            } else if (nr == maxim && hand.get(i) < Constants.NO_OF_LEGAL_GOODS
                    && inventory.getGoodsById(chosenID).getProfit()
                    == inventory.getGoodsById(hand.get(i)).getProfit()
                    && chosenID < hand.get(i)) {
                chosenID = hand.get(i);
            }

            if (nr > maxim && hand.get(i) < Constants.NO_OF_LEGAL_GOODS) {
                maxim = nr;
                chosenID = hand.get(i);
            }
        }
        if (maxim > Constants.NO_OF_CARDS_IN_BAG) {
            maxim = Constants.NO_OF_CARDS_IN_BAG;
        }
        // Add cards to bag and remove them from hand
        for (int i = 0; i < maxim; i++) {
            hand.remove(chosenID);
            bag.add(chosenID);
        }
        // If there are no legal goods find the most profitable illegal one
        if (maxim == -1 && money >= Constants.MONEY_LIMIT) {
            int maxprofit = -1;
            for (Integer i : hand) {
                if (inventory.getGoodsById(i).getProfit() > maxprofit) {
                    maxprofit = inventory.getGoodsById(i).getProfit();
                    chosenID = i;
                }
            }
            hand.remove(chosenID);
            bag.add(chosenID);
        }
    }
    // The method to be called for all types of players
    public final void createBag() {
        if (basic) {
            makeBag();
        }
        if (greedy) {
            makeBag();
            int maxprofit = -1;
            Integer chosenId = -1;
            // If the round number is even add an illegal card
            // Find the one with biggest profit
            if (getBag().size() < Constants.NO_OF_CARDS_IN_BAG
                    && getRoundNumber() % 2 == 0 && getMoney() >= Constants.ILLEGAL_PENALTY) {
                for (int i = 0; i < getHand().size(); i++) {
                    if (getInventory().getGoodsById(getHand().get(i)).getProfit()
                            == maxprofit && getHand().get(i) > Constants.NO_OF_LEGAL_GOODS
                            && getHand().get(i) > chosenId) {
                        chosenId = getHand().get(i);
                    }
                    if (getInventory().getGoodsById(getHand().get(i)).getProfit()
                            > maxprofit && getHand().get(i) > Constants.NO_OF_LEGAL_GOODS) {
                        maxprofit = getInventory().getGoodsById(getHand().get(i)).getProfit();
                        chosenId = getHand().get(i);
                    }
                }
            }
            // If one is found add to Bag and remove from Hand
            if (chosenId != -1) {
                getHand().remove(chosenId);
                getBag().add(chosenId);
            }
        }
        if (bribe) {
            getBag().clear();
            int illegals = 0;
            for (Integer i : getHand()) {
                if (i > Constants.NO_OF_LEGAL_GOODS) {
                    illegals++;
                }
            }
            int contillegals = 0;
            // Add as many illegal goods as the player affords to pay for
            // Add them in decreasing order of their profit
            while (illegals > 0 && getMoney() > Constants.ILLEGAL_PENALTY * (contillegals + 1)
                    && contillegals < Constants.NO_OF_CARDS_IN_BAG
                    && getMoney() > Constants.MIN_BRIBE) {
                int maxprofit = -1;
                Integer chosenId = -1;
                for (Integer i : getHand()) {
                    if (i > Constants.NO_OF_LEGAL_GOODS
                            && getInventory().getGoodsById(i).getProfit()
                            == maxprofit && i > chosenId) {
                        maxprofit = getInventory().getGoodsById(i).getProfit();
                        chosenId = i;
                    }
                    if (i > Constants.NO_OF_LEGAL_GOODS
                            && getInventory().getGoodsById(i).getProfit() > maxprofit) {
                        maxprofit = getInventory().getGoodsById(i).getProfit();
                        chosenId = i;
                    }
                }
                getBag().add(chosenId);
                getHand().remove(chosenId);
                contillegals++;
                illegals--;
            }
            // If Bag is not full add the legal goods with biggest profit
            int contlegals = 0;
            if (contillegals == 0) {
                makeBag();
                setGivenBribe(0);
            } else {
                while (getMoney() > Constants.ILLEGAL_PENALTY * contillegals + 2 * (contlegals + 1)
                        && contlegals + contillegals < Constants.NO_OF_CARDS_IN_BAG) {
                    int maxprofit = -1;
                    Integer chosenId = -1;
                    for (Integer i : getHand()) {
                        if (i < Constants.NO_OF_LEGAL_GOODS
                                && getInventory().getGoodsById(i).getProfit()
                                == maxprofit && i > chosenId) {
                            maxprofit = getInventory().getGoodsById(i).getProfit();
                            chosenId = i;
                        }
                        if (i < Constants.NO_OF_LEGAL_GOODS
                                && getInventory().getGoodsById(i).getProfit() > maxprofit) {
                            maxprofit = getInventory().getGoodsById(i).getProfit();
                            chosenId = i;
                        }
                    }
                    if (chosenId == -1) {
                        break;
                    } else {
                        getBag().add(chosenId);
                        getHand().remove(chosenId);
                        contlegals++;
                    }
                }
            }
            // Depending on the number of illegal goods set a bribe
            setGivenBribe(0);
            if (contillegals > 2) {
                setGivenBribe(Constants.MAX_BRIBE);
            }
            if (contillegals > 0 && contillegals <= 2) {
                setGivenBribe(Constants.MIN_BRIBE);
            }

        }

    }

    // The method for checking another player's bag as sheriff
    private void checkPlayer(final BasePlayer player) {
        // If the checked player is basic
        // If the bag has only one illegal product, he lied so he pays penalty
        // If the bag only has legal goods, the sheriff pays penalty
        if (player.basic) {
            if (player.bag.size() == 1 && player.bag.get(0) > Constants.NO_OF_LEGAL_GOODS) {
                assets.add(player.bag.get(0));
                money += Constants.ILLEGAL_PENALTY;
                player.money -= Constants.ILLEGAL_PENALTY;
            } else {
                money -= Constants.LEGAL_PENALTY * player.bag.size();
                player.money += Constants.LEGAL_PENALTY * player.bag.size();
                player.stand.addAll(player.bag);
            }
        }
        // If the player is greedy
        // If the bag contains illegal products, the players pays penalty for them
        // The legal ones go to the stand
        // If the bag only has legal goods, the sheriff pays penalty
        if (player.greedy) {
            int ok = 1;
            for (Integer i : player.bag) {
                if (i > Constants.NO_OF_LEGAL_GOODS) {
                    ok = 0;
                    break;
                }
            }
            if (ok == 0) {
                for (Integer i : player.bag) {
                    if (i > Constants.NO_OF_LEGAL_GOODS) {
                        money += Constants.ILLEGAL_PENALTY;
                        player.money -= Constants.ILLEGAL_PENALTY;
                        assets.add(i);
                    } else {
                        player.stand.add(i);
                    }
                }
            } else {
                for (Integer i : player.bag) {
                    money -= Constants.LEGAL_PENALTY;
                    player.money += Constants.LEGAL_PENALTY;
                    player.stand.add(i);
                }
            }
        }
        // If the player is bribed
        // If the bag contains illegal products, the players pays penalty for them
        // and also for any undeclared legal good
        // Only apples go to the stand
        // If the bag only has legal goods, the sheriff pays penalty
        if (player.bribe) {
            boolean hasIllegals = false;
            for (Integer i : player.bag) {
                if (i > Constants.NO_OF_LEGAL_GOODS) {
                    hasIllegals = true;
                    break;
                }
            }
            if (hasIllegals) {
                for (Integer i : player.bag) {
                    if (i != 0) {
                        if (i < Constants.NO_OF_LEGAL_GOODS) {
                            money += 2;
                            player.money -= 2;
                            assets.add(i);
                        } else {
                            money += Constants.ILLEGAL_PENALTY;
                            player.money -= Constants.ILLEGAL_PENALTY;
                            assets.add(i);
                        }
                    } else {

                        player.stand.add(i);
                    }
                }
            } else {
                for (Integer i : player.bag) {
                    money -= 2;
                    player.money += 2;
                    player.stand.add(i);
                }
            }
        }
    }

    public final int getNoOfGoodsById(final int idGood) {
        return Collections.frequency(stand, idGood);
    }
    // Add on the stand the cards provided by the illegal bonus
    public final void addIllegalBonuses() {
        int n = stand.size();
        for (int i = 0; i < n; i++) {
            // for(int kk = 0; kk < Players.get(k).Stand.size(); kk++) {
            //   System.out.println(Stand.get(i));
            if (stand.get(i) > Constants.NO_OF_LEGAL_GOODS) {
                switch (stand.get(i)) {
                    case Constants.SILK_ID:
                        stand.add(Constants.CHEESE_ID);
                        stand.add(Constants.CHEESE_ID);
                        stand.add(Constants.CHEESE_ID);
                        break;
                    case Constants.PEPPER_ID:
                        stand.add(Constants.CHICKEN_ID);
                        stand.add(Constants.CHICKEN_ID);
                        break;
                    case Constants.BARREL_ID:
                        stand.add(Constants.BREAD_ID);
                        stand.add(Constants.BREAD_ID);
                        break;
                    case Constants.BEER_ID:
                        stand.add(Constants.WINE_ID);
                        stand.add(Constants.WINE_ID);
                        stand.add(Constants.WINE_ID);
                        stand.add(Constants.WINE_ID);
                        break;
                    case Constants.SEAFOOD_ID:
                        stand.add(Constants.TOMATO_ID);
                        stand.add(Constants.TOMATO_ID);
                        stand.add(Constants.POTATO_ID);
                        stand.add(Constants.POTATO_ID);
                        stand.add(Constants.POTATO_ID);
                        stand.add(Constants.CHICKEN_ID);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void takeBribe(final BasePlayer player) {
        money += player.getGivenBribe();
        player.money -= player.getGivenBribe();
    }
    // The methods has as parameter the whole list of players
    // The sheriff chooses from the list whose bag to check
    public final void beSheriff(final int index, final ArrayList<BasePlayer> players) {
        // If the player is basic he will check all players
        if (basic) {
            for (int k = 0; k < players.size(); k++) {
                if (index != k) {
                    // He only checks if he has enough money to pay penalty
                    if (money >= Constants.SHERIFF_MONEY_LIMIT) {
                        checkPlayer(players.get(k));
                    }
                }
            }
        }
        // If the player is greedy he will check all players that don't give bribe
        if (greedy) {
            for (int k = 0; k < players.size(); k++) {
                if (index != k) {
                    if (players.get(k).bribe
                            && (players.get(k).getGivenBribe() != 0)) {
                        takeBribe(players.get(k));
                        players.get(k).stand.addAll(players.get(k).bag);
                    } else {
                        // He only checks if he has enough money to pay penalty
                        if (money >= Constants.SHERIFF_MONEY_LIMIT) {
                            checkPlayer(players.get(k));
                        } else {
                            players.get(k).stand.addAll(players.get(k).bag);
                        }
                    }
                }
            }
        }
        // If the player is bribed he will check the players to his left and right
        // Then he takes bribe from anyone who offers
        if (bribe) {
            if (index == 0) {
                if (money >= Constants.SHERIFF_MONEY_LIMIT) {
                    checkPlayer(players.get(players.size() - 1));
                } else {
                    players.get(players.size() - 1).stand.addAll(players.get(players.size()
                            - 1).bag);
                }
                if (players.size() > 2) {
                    if (money >= Constants.SHERIFF_MONEY_LIMIT) {
                        checkPlayer(players.get(1));
                    } else {
                        players.get(1).stand.addAll(players.get(1).bag);
                    }
                }
            } else {
                if (index == players.size() - 1) {
                    if (money >= Constants.SHERIFF_MONEY_LIMIT) {
                        checkPlayer(players.get(index - 1));
                    } else {
                        players.get(index - 1).stand.addAll(players.get(index - 1).bag);
                    }
                    if (players.size() > 2) {
                        if (money >= Constants.SHERIFF_MONEY_LIMIT) {
                            checkPlayer(players.get(0));
                        } else {
                            players.get(0).stand.addAll(players.get(0).bag);
                        }
                    }
                } else {
                    if (money >= Constants.SHERIFF_MONEY_LIMIT) {
                        checkPlayer(players.get(index - 1));
                    } else {
                        players.get(index - 1).stand.addAll(players.get(index - 1).bag);
                    }
                    if (money >= Constants.SHERIFF_MONEY_LIMIT) {
                        checkPlayer(players.get(index + 1));
                    } else {
                        players.get(index + 1).stand.addAll(players.get(index + 1).bag);
                    }
                }
            }
            for (int k = 0; k < players.size(); k++) {
                if (index != k) {
                    if (!((index == 0 && k == players.size() - 1)
                            || (index == players.size() - 1 && k == 0)
                            || k == index - 1 || k == index + 1)) {
                        if (players.get(k).bribe && players.get(k).getGivenBribe() != 0) {
                            takeBribe(players.get(k));
                        }
                        players.get(k).stand.addAll(players.get(k).bag);
                    }
                }
            }
        }
    }

    public final void addProfit() {
        for (Integer i : stand) {
            money += inventory.getGoodsById(i).getProfit();
        }
    }
}
