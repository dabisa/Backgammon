package com.dkelava.backgammon.bglib.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DieFaceTest {

    @Test
    public void valueOf() {
        //assertEquals(Die.Face.valueOf(0), Die.Face.None);
        assertEquals(Die.Face.valueOf(1), Die.Face.Die1);
        assertEquals(Die.Face.valueOf(2), Die.Face.Die2);
        assertEquals(Die.Face.valueOf(3), Die.Face.Die3);
        assertEquals(Die.Face.valueOf(4), Die.Face.Die4);
        assertEquals(Die.Face.valueOf(5), Die.Face.Die5);
        assertEquals(Die.Face.valueOf(6), Die.Face.Die6);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void valueOf_assert1() {
        Die.Face.valueOf(0);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void valueOf_assert2() {
        Die.Face.valueOf(7);
    }

    @Test
    public void getValue() {
        //assertEquals(Die.Face.None.getValue(), 0);
        assertEquals(Die.Face.Die1.getValue(), 1);
        assertEquals(Die.Face.Die2.getValue(), 2);
        assertEquals(Die.Face.Die3.getValue(), 3);
        assertEquals(Die.Face.Die4.getValue(), 4);
        assertEquals(Die.Face.Die5.getValue(), 5);
        assertEquals(Die.Face.Die6.getValue(), 6);
    }
}
