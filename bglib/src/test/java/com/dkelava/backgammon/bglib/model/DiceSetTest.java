package com.dkelava.backgammon.bglib.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DiceSetTest {

    private void testCreateRoll(Die.Face dieOne, Die.Face dieTwo) {
        DiceSet diceSet = DiceSet.createRoll(dieOne, dieTwo);
        assertNotNull(diceSet);

        if (dieOne != dieTwo) {
            assertEquals(diceSet.getTotalMoves(), 2);
        } else {
            assertEquals(diceSet.getTotalMoves(), 4);
        }

        for (Die.Face die : Die.Face.values()) {
            if (die == dieOne && die == dieTwo) {
                assertEquals(diceSet.getNumberOfMoves(die), 4);
            } else if (die == dieOne) {
                assertEquals(diceSet.getNumberOfMoves(die), 1);
            } else if (die == dieTwo) {
                assertEquals(diceSet.getNumberOfMoves(die), 1);
            } else {
                assertEquals(diceSet.getNumberOfMoves(die), 0);
            }
        }

        List<Die.Face> dice = diceSet.getDice();
        if (dieOne == dieTwo) {
            assertEquals(dice.size(), 1);
            assertTrue(dice.contains(dieOne));
        } else {
            assertEquals(dice.size(), 2);
            assertTrue(dice.contains(dieOne));
            assertTrue(dice.contains(dieTwo));

        }
    }

    private void testCreateCustom(Die.Face dieOne, int dieOneMoves, Die.Face dieTwo, int dieTwoMoves) {
        DiceSet diceSet = DiceSet.createCustom(dieOne, dieOneMoves, dieTwo, dieTwoMoves);
        assertNotNull(diceSet);

        assertEquals(diceSet.getTotalMoves(), dieOneMoves + dieTwoMoves);

        for (Die.Face die : Die.Face.values()) {
            if (die == dieOne && die == dieTwo) {
                assertEquals(diceSet.getNumberOfMoves(die), dieOneMoves + dieTwoMoves);
            } else if (die == dieOne) {
                assertEquals(diceSet.getNumberOfMoves(die), dieOneMoves);
            } else if (die == dieTwo) {
                assertEquals(diceSet.getNumberOfMoves(die), dieTwoMoves);
            } else {
                assertEquals(diceSet.getNumberOfMoves(die), 0);
            }
        }

        List<Die.Face> dice = diceSet.getDice();
        if (dieOne != dieTwo) {
            if (dieOneMoves > 0 && dieTwoMoves > 0) {
                assertEquals(dice.size(), 2);
                assertTrue(dice.contains(dieOne));
                assertTrue(dice.contains(dieTwo));
            } else if (dieOneMoves > 0) {
                assertEquals(dice.size(), 1);
                assertTrue(dice.contains(dieOne));
                assertFalse(dice.contains(dieTwo));
            } else if (dieTwoMoves > 0) {
                assertEquals(dice.size(), 1);
                assertFalse(dice.contains(dieOne));
                assertTrue(dice.contains(dieTwo));
            } else {
                assertEquals(dice.size(), 0);
                assertFalse(dice.contains(dieOne));
                assertFalse(dice.contains(dieTwo));
            }
        } else {
            if (dieOneMoves + dieTwoMoves > 0) {
                assertEquals(dice.size(), 1);
                assertTrue(dice.contains(dieOne));
            } else {
                assertEquals(dice.size(), 0);
                assertFalse(dice.contains(dieOne));
            }
        }
    }

    private void testCreateCustom(Die.Face die, int dieMoves) {
        testCreateCustom(die, (dieMoves + 1) / 2, die, dieMoves / 2);
    }

    @Test
    public void createEmpty() {
        DiceSet diceSet = DiceSet.createEmpty();
        assertNotNull(diceSet);
        assertEquals(diceSet.getTotalMoves(), 0);
        assertEquals(diceSet.getDice().size(), 0);
    }

    @Test
    public void createRoll() {
        testCreateRoll(Die.Face.Die1, Die.Face.Die6);
        testCreateRoll(Die.Face.Die3, Die.Face.Die3);
    }

    @Test
    public void createCustom() {
        testCreateCustom(Die.Face.Die1, 0, Die.Face.Die6, 0);
        testCreateCustom(Die.Face.Die2, 0, Die.Face.Die5, 1);
        testCreateCustom(Die.Face.Die3, 1, Die.Face.Die4, 0);
        testCreateCustom(Die.Face.Die4, 1, Die.Face.Die3, 1);
        testCreateCustom(Die.Face.Die5, 0);
        testCreateCustom(Die.Face.Die5, 1);
        testCreateCustom(Die.Face.Die5, 2);
        testCreateCustom(Die.Face.Die6, 3);
        testCreateCustom(Die.Face.Die6, 4);
    }

    @Test
    public void equals_add_remove() {
        DiceSet state1 = DiceSet.createCustom(Die.Face.Die1, 1, Die.Face.Die2, 0);
        DiceSet state2 = DiceSet.createCustom(Die.Face.Die1, 1, Die.Face.Die2, 0);
        assertEquals(state1, state2);
        state1.add(Die.Face.Die1);
        assertNotEquals(state1, state2);
        state1.remove(Die.Face.Die1);
        assertEquals(state1, state2);
        state1.remove(Die.Face.Die1);
        assertNotEquals(state1, state2);
        state2.remove(Die.Face.Die1);
        assertEquals(state1, state2);

        state1 = DiceSet.createRoll(Die.Face.Die6, Die.Face.Die5);
        state2 = DiceSet.createRoll(Die.Face.Die5, Die.Face.Die6);
        assertEquals(state1, state2);
        state1.add(Die.Face.Die5);
        assertNotEquals(state1, state2);
        state1.remove(Die.Face.Die5);
        assertEquals(state1, state2);
        state1.remove(Die.Face.Die5);
        assertNotEquals(state1, state2);
        state2.remove(Die.Face.Die5);
        assertEquals(state1, state2);

    }

    @Test
    public void hashCodeTest() {
        // TODO: 14.2.2017.
    }
}
