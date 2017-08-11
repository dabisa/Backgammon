package com.dkelava.backgammon.bglib.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * DiceSet represents collection of dice that player can use to make moves.
 */
public class DiceSet {

    private final HashMap<Die.Face, Integer> _dice;

    private DiceSet(HashMap<Die.Face, Integer> dice) {
        _dice = dice;
    }

    /**
     * Creates empty DiceSet
     */
    public static DiceSet createEmpty() {
        return new DiceSet(new HashMap<Die.Face, Integer>());
    }

    /**
     * Creates DiceSet that contains dice moves that are available after rolling specified dice.
     *
     * If player rolls two different dice then DiceSets contains these two dice.
     * If player rolls both dice with the same value, DiceSet contains four dice of specified dice.
     */
    public static DiceSet createRoll(Die.Face dieOne, Die.Face dieTwo) {
        HashMap<Die.Face, Integer> dice = new HashMap<>();
        if (dieOne == dieTwo) {
            dice.put(dieOne, 4);
        } else {
            dice.put(dieOne, 1);
            dice.put(dieTwo, 1);
        }
        return new DiceSet(dice);
    }

    /**
     * Creates DiceSet with specified dice and their quentities.
     */
    public static DiceSet createCustom(Die.Face dieOne, int movesOne, Die.Face dieTwo, int movesTwo) {
        HashMap<Die.Face, Integer> dice = new HashMap<>();
        if (dieOne == dieTwo) {
            if (movesOne + movesTwo > 0) {
                dice.put(dieOne, movesOne + movesTwo);
            }
        } else {
            if (movesOne > 0) {
                dice.put(dieOne, movesOne);
            }
            if (movesTwo > 0) {
                dice.put(dieTwo, movesTwo);
            }
        }
        return new DiceSet(dice);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DiceSet) {
            DiceSet other = (DiceSet) o;
            boolean result = (this._dice.equals(other._dice));
            return result;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        // TODO: 14.2.2017.  check this
        return this._dice.hashCode();
    }

    /**
     * Removes specified die from DiceSet
     */
    public final void remove(Die.Face die) {
        if (_dice.get(die) - 1 <= 0) {
            _dice.remove(die);
        } else {
            _dice.put(die, _dice.get(die) - 1);
        }
    }

    /**
     * Adds specified die to DiceSet
     */
    public final void add(Die.Face die) {
        if (!_dice.containsKey(die)) {
            _dice.put(die, 1);
        } else {
            _dice.put(die, _dice.get(die) + 1);
        }
    }

    /**
     * Returns list of distinct dice in DiceSet
     */
    public final List<Die.Face> getDice() {
        List<Die.Face> dice = new LinkedList<>();
        for (Map.Entry<Die.Face, Integer> entry : _dice.entrySet()) {
            if (entry.getValue() > 0) {
                dice.add(entry.getKey());
            }
        }
        return dice;
    }

    /**
     * Returns total number of moves that can be performed with this DiceSet
     */
    public final int getTotalMoves() {
        int total = 0;
        for (Map.Entry<Die.Face, Integer> entry : _dice.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    public final int getTotalDistance() {
        int total = 0;
        for (Map.Entry<Die.Face, Integer> entry : _dice.entrySet()) {
            total += entry.getValue() * entry.getKey().getValue();
        }
        return total;
    }

    /**
     * Returns number of moves that can be done with specified die using this DiceSet
     */
    public final int getNumberOfMoves(Die.Face die) {
        if (_dice.containsKey(die)) {
            return _dice.get(die);
        } else {
            return 0;
        }
    }
}
