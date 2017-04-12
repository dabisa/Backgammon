package com.dkelava.bglib.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColorTest {

    @Test
    public void getOpponent() {
        assertEquals(Color.None.getOpponent(), Color.None);
        assertEquals(Color.White.getOpponent(), Color.Black);
        assertEquals(Color.Black.getOpponent(), Color.White);
    }
}
