package com.dkelava.bglib.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PointStateTest {
    @Test
    public void valueOf() {
        assertEquals(PointState.Empty, PointState.valueOf(Color.None, 0));
        assertEquals(PointState.White1, PointState.valueOf(Color.White, 1));
        assertEquals(PointState.White2, PointState.valueOf(Color.White, 2));
        assertEquals(PointState.White3, PointState.valueOf(Color.White, 3));
        assertEquals(PointState.White4, PointState.valueOf(Color.White, 4));
        assertEquals(PointState.White5, PointState.valueOf(Color.White, 5));
        assertEquals(PointState.White6, PointState.valueOf(Color.White, 6));
        assertEquals(PointState.White7, PointState.valueOf(Color.White, 7));
        assertEquals(PointState.White8, PointState.valueOf(Color.White, 8));
        assertEquals(PointState.White9, PointState.valueOf(Color.White, 9));
        assertEquals(PointState.White10, PointState.valueOf(Color.White, 10));
        assertEquals(PointState.White11, PointState.valueOf(Color.White, 11));
        assertEquals(PointState.White12, PointState.valueOf(Color.White, 12));
        assertEquals(PointState.White13, PointState.valueOf(Color.White, 13));
        assertEquals(PointState.White14, PointState.valueOf(Color.White, 14));
        assertEquals(PointState.White15, PointState.valueOf(Color.White, 15));
        assertEquals(PointState.Black1, PointState.valueOf(Color.Black, 1));
        assertEquals(PointState.Black2, PointState.valueOf(Color.Black, 2));
        assertEquals(PointState.Black3, PointState.valueOf(Color.Black, 3));
        assertEquals(PointState.Black4, PointState.valueOf(Color.Black, 4));
        assertEquals(PointState.Black5, PointState.valueOf(Color.Black, 5));
        assertEquals(PointState.Black6, PointState.valueOf(Color.Black, 6));
        assertEquals(PointState.Black7, PointState.valueOf(Color.Black, 7));
        assertEquals(PointState.Black8, PointState.valueOf(Color.Black, 8));
        assertEquals(PointState.Black9, PointState.valueOf(Color.Black, 9));
        assertEquals(PointState.Black10, PointState.valueOf(Color.Black, 10));
        assertEquals(PointState.Black11, PointState.valueOf(Color.Black, 11));
        assertEquals(PointState.Black12, PointState.valueOf(Color.Black, 12));
        assertEquals(PointState.Black13, PointState.valueOf(Color.Black, 13));
        assertEquals(PointState.Black14, PointState.valueOf(Color.Black, 14));
        assertEquals(PointState.Black15, PointState.valueOf(Color.Black, 15));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void valueOf_assertion1() {
        PointState.valueOf(Color.Black, 0);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void valueOf_assertion2() {
        PointState.valueOf(Color.Black, 16);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void valueOf_assertion3() {
        PointState.valueOf(Color.White, 0);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void valueOf_assertion4() {
        PointState.valueOf(Color.White, 16);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void valueOf_assertion5() {
        PointState.valueOf(Color.None, 1);
    }

    @Test
    public void add() {
        assertEquals(PointState.Empty.add(Color.White), PointState.White1);
        assertEquals(PointState.White1.add(Color.White), PointState.White2);
        assertEquals(PointState.White2.add(Color.White), PointState.White3);
        assertEquals(PointState.White3.add(Color.White), PointState.White4);
        assertEquals(PointState.White4.add(Color.White), PointState.White5);
        assertEquals(PointState.White5.add(Color.White), PointState.White6);
        assertEquals(PointState.White6.add(Color.White), PointState.White7);
        assertEquals(PointState.White7.add(Color.White), PointState.White8);
        assertEquals(PointState.White8.add(Color.White), PointState.White9);
        assertEquals(PointState.White9.add(Color.White), PointState.White10);
        assertEquals(PointState.White10.add(Color.White), PointState.White11);
        assertEquals(PointState.White11.add(Color.White), PointState.White12);
        assertEquals(PointState.White12.add(Color.White), PointState.White13);
        assertEquals(PointState.White13.add(Color.White), PointState.White14);
        assertEquals(PointState.White14.add(Color.White), PointState.White15);

        assertEquals(PointState.Empty.add(Color.Black), PointState.Black1);
        assertEquals(PointState.Black1.add(Color.Black), PointState.Black2);
        assertEquals(PointState.Black2.add(Color.Black), PointState.Black3);
        assertEquals(PointState.Black3.add(Color.Black), PointState.Black4);
        assertEquals(PointState.Black4.add(Color.Black), PointState.Black5);
        assertEquals(PointState.Black5.add(Color.Black), PointState.Black6);
        assertEquals(PointState.Black6.add(Color.Black), PointState.Black7);
        assertEquals(PointState.Black7.add(Color.Black), PointState.Black8);
        assertEquals(PointState.Black8.add(Color.Black), PointState.Black9);
        assertEquals(PointState.Black9.add(Color.Black), PointState.Black10);
        assertEquals(PointState.Black10.add(Color.Black), PointState.Black11);
        assertEquals(PointState.Black11.add(Color.Black), PointState.Black12);
        assertEquals(PointState.Black12.add(Color.Black), PointState.Black13);
        assertEquals(PointState.Black13.add(Color.Black), PointState.Black14);
        assertEquals(PointState.Black14.add(Color.Black), PointState.Black15);

    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void add_assertion1() {
        PointState.Empty.add(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void add_assertion2() {
        PointState.White1.add(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void add_assertion3() {
        PointState.Black1.add(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void add_assertion4() {
        PointState.White1.add(Color.Black);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void add_assertion5() {
        PointState.Black1.add(Color.White);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void add_assertion6() {
        PointState.White15.add(Color.White);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void add_assertion7() {
        PointState.Black15.add(Color.Black);
    }

    @Test
    public void remove() {
        assertEquals(PointState.White1.remove(Color.White), PointState.Empty);
        assertEquals(PointState.White2.remove(Color.White), PointState.White1);
        assertEquals(PointState.White3.remove(Color.White), PointState.White2);
        assertEquals(PointState.White4.remove(Color.White), PointState.White3);
        assertEquals(PointState.White5.remove(Color.White), PointState.White4);
        assertEquals(PointState.White6.remove(Color.White), PointState.White5);
        assertEquals(PointState.White7.remove(Color.White), PointState.White6);
        assertEquals(PointState.White8.remove(Color.White), PointState.White7);
        assertEquals(PointState.White9.remove(Color.White), PointState.White8);
        assertEquals(PointState.White10.remove(Color.White), PointState.White9);
        assertEquals(PointState.White11.remove(Color.White), PointState.White10);
        assertEquals(PointState.White12.remove(Color.White), PointState.White11);
        assertEquals(PointState.White13.remove(Color.White), PointState.White12);
        assertEquals(PointState.White14.remove(Color.White), PointState.White13);
        assertEquals(PointState.White15.remove(Color.White), PointState.White14);

        assertEquals(PointState.Black1.remove(Color.Black), PointState.Empty);
        assertEquals(PointState.Black2.remove(Color.Black), PointState.Black1);
        assertEquals(PointState.Black3.remove(Color.Black), PointState.Black2);
        assertEquals(PointState.Black4.remove(Color.Black), PointState.Black3);
        assertEquals(PointState.Black5.remove(Color.Black), PointState.Black4);
        assertEquals(PointState.Black6.remove(Color.Black), PointState.Black5);
        assertEquals(PointState.Black7.remove(Color.Black), PointState.Black6);
        assertEquals(PointState.Black8.remove(Color.Black), PointState.Black7);
        assertEquals(PointState.Black9.remove(Color.Black), PointState.Black8);
        assertEquals(PointState.Black10.remove(Color.Black), PointState.Black9);
        assertEquals(PointState.Black11.remove(Color.Black), PointState.Black10);
        assertEquals(PointState.Black12.remove(Color.Black), PointState.Black11);
        assertEquals(PointState.Black13.remove(Color.Black), PointState.Black12);
        assertEquals(PointState.Black14.remove(Color.Black), PointState.Black13);
        assertEquals(PointState.Black15.remove(Color.Black), PointState.Black14);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void remove_assertion1() {
        PointState.Empty.remove(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void remove_assertion2() {
        PointState.White1.remove(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void remove_assertion3() {
        PointState.Black1.remove(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void remove_assertion4() {
        PointState.White1.remove(Color.Black);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void remove_assertion5() {
        PointState.Black1.remove(Color.White);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void remove_assertion6() {
        PointState.Empty.remove(Color.White);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void remove_assertion7() {
        PointState.Empty.remove(Color.Black);
    }

    @Test
    public void getOwner() {
        assertEquals(PointState.Empty.getOwner(), Color.None);

        assertEquals(PointState.White1.getOwner(), Color.White);
        assertEquals(PointState.White2.getOwner(), Color.White);
        assertEquals(PointState.White3.getOwner(), Color.White);
        assertEquals(PointState.White4.getOwner(), Color.White);
        assertEquals(PointState.White5.getOwner(), Color.White);
        assertEquals(PointState.White6.getOwner(), Color.White);
        assertEquals(PointState.White7.getOwner(), Color.White);
        assertEquals(PointState.White8.getOwner(), Color.White);
        assertEquals(PointState.White9.getOwner(), Color.White);
        assertEquals(PointState.White10.getOwner(), Color.White);
        assertEquals(PointState.White11.getOwner(), Color.White);
        assertEquals(PointState.White12.getOwner(), Color.White);
        assertEquals(PointState.White13.getOwner(), Color.White);
        assertEquals(PointState.White14.getOwner(), Color.White);
        assertEquals(PointState.White15.getOwner(), Color.White);

        assertEquals(PointState.Black1.getOwner(), Color.Black);
        assertEquals(PointState.Black2.getOwner(), Color.Black);
        assertEquals(PointState.Black3.getOwner(), Color.Black);
        assertEquals(PointState.Black4.getOwner(), Color.Black);
        assertEquals(PointState.Black5.getOwner(), Color.Black);
        assertEquals(PointState.Black6.getOwner(), Color.Black);
        assertEquals(PointState.Black7.getOwner(), Color.Black);
        assertEquals(PointState.Black8.getOwner(), Color.Black);
        assertEquals(PointState.Black9.getOwner(), Color.Black);
        assertEquals(PointState.Black10.getOwner(), Color.Black);
        assertEquals(PointState.Black11.getOwner(), Color.Black);
        assertEquals(PointState.Black12.getOwner(), Color.Black);
        assertEquals(PointState.Black13.getOwner(), Color.Black);
        assertEquals(PointState.Black14.getOwner(), Color.Black);
        assertEquals(PointState.Black15.getOwner(), Color.Black);
    }

    @Test
    public void isBlot() {
        assertFalse(PointState.Empty.isBlot());
        assertTrue(PointState.White1.isBlot());
        assertFalse(PointState.White2.isBlot());
        assertFalse(PointState.White3.isBlot());
        assertFalse(PointState.White4.isBlot());
        assertFalse(PointState.White5.isBlot());
        assertFalse(PointState.White6.isBlot());
        assertFalse(PointState.White7.isBlot());
        assertFalse(PointState.White8.isBlot());
        assertFalse(PointState.White9.isBlot());
        assertFalse(PointState.White10.isBlot());
        assertFalse(PointState.White11.isBlot());
        assertFalse(PointState.White12.isBlot());
        assertFalse(PointState.White13.isBlot());
        assertFalse(PointState.White14.isBlot());
        assertFalse(PointState.White15.isBlot());
        assertTrue(PointState.Black1.isBlot());
        assertFalse(PointState.Black2.isBlot());
        assertFalse(PointState.Black3.isBlot());
        assertFalse(PointState.Black4.isBlot());
        assertFalse(PointState.Black5.isBlot());
        assertFalse(PointState.Black6.isBlot());
        assertFalse(PointState.Black7.isBlot());
        assertFalse(PointState.Black8.isBlot());
        assertFalse(PointState.Black9.isBlot());
        assertFalse(PointState.Black10.isBlot());
        assertFalse(PointState.Black11.isBlot());
        assertFalse(PointState.Black12.isBlot());
        assertFalse(PointState.Black13.isBlot());
        assertFalse(PointState.Black14.isBlot());
        assertFalse(PointState.Black15.isBlot());
    }

    @Test
    public void canHit() {
        assertFalse(PointState.Empty.canHit(Color.None));
        assertFalse(PointState.Empty.canHit(Color.White));
        assertFalse(PointState.Empty.canHit(Color.Black));

        assertFalse(PointState.White1.canHit(Color.None));
        assertFalse(PointState.White1.canHit(Color.White));
        assertTrue(PointState.White1.canHit(Color.Black));
        assertFalse(PointState.White2.canHit(Color.None));
        assertFalse(PointState.White2.canHit(Color.White));
        assertFalse(PointState.White2.canHit(Color.Black));
        assertFalse(PointState.White3.canHit(Color.None));
        assertFalse(PointState.White3.canHit(Color.White));
        assertFalse(PointState.White3.canHit(Color.Black));
        assertFalse(PointState.White4.canHit(Color.None));
        assertFalse(PointState.White4.canHit(Color.White));
        assertFalse(PointState.White4.canHit(Color.Black));
        assertFalse(PointState.White5.canHit(Color.None));
        assertFalse(PointState.White5.canHit(Color.White));
        assertFalse(PointState.White5.canHit(Color.Black));
        assertFalse(PointState.White6.canHit(Color.None));
        assertFalse(PointState.White6.canHit(Color.White));
        assertFalse(PointState.White6.canHit(Color.Black));
        assertFalse(PointState.White7.canHit(Color.None));
        assertFalse(PointState.White7.canHit(Color.White));
        assertFalse(PointState.White7.canHit(Color.Black));
        assertFalse(PointState.White8.canHit(Color.None));
        assertFalse(PointState.White8.canHit(Color.White));
        assertFalse(PointState.White8.canHit(Color.Black));
        assertFalse(PointState.White9.canHit(Color.None));
        assertFalse(PointState.White9.canHit(Color.White));
        assertFalse(PointState.White9.canHit(Color.Black));
        assertFalse(PointState.White10.canHit(Color.None));
        assertFalse(PointState.White10.canHit(Color.White));
        assertFalse(PointState.White10.canHit(Color.Black));
        assertFalse(PointState.White11.canHit(Color.None));
        assertFalse(PointState.White11.canHit(Color.White));
        assertFalse(PointState.White11.canHit(Color.Black));
        assertFalse(PointState.White12.canHit(Color.None));
        assertFalse(PointState.White12.canHit(Color.White));
        assertFalse(PointState.White12.canHit(Color.Black));
        assertFalse(PointState.White13.canHit(Color.None));
        assertFalse(PointState.White13.canHit(Color.White));
        assertFalse(PointState.White13.canHit(Color.Black));
        assertFalse(PointState.White14.canHit(Color.None));
        assertFalse(PointState.White14.canHit(Color.White));
        assertFalse(PointState.White14.canHit(Color.Black));
        assertFalse(PointState.White15.canHit(Color.None));
        assertFalse(PointState.White15.canHit(Color.White));
        assertFalse(PointState.White15.canHit(Color.Black));

        assertFalse(PointState.Black1.canHit(Color.None));
        assertTrue(PointState.Black1.canHit(Color.White));
        assertFalse(PointState.Black1.canHit(Color.Black));
        assertFalse(PointState.Black2.canHit(Color.None));
        assertFalse(PointState.Black2.canHit(Color.White));
        assertFalse(PointState.Black2.canHit(Color.Black));
        assertFalse(PointState.Black3.canHit(Color.None));
        assertFalse(PointState.Black3.canHit(Color.White));
        assertFalse(PointState.Black3.canHit(Color.Black));
        assertFalse(PointState.Black4.canHit(Color.None));
        assertFalse(PointState.Black4.canHit(Color.White));
        assertFalse(PointState.Black4.canHit(Color.Black));
        assertFalse(PointState.Black5.canHit(Color.None));
        assertFalse(PointState.Black5.canHit(Color.White));
        assertFalse(PointState.Black5.canHit(Color.Black));
        assertFalse(PointState.Black6.canHit(Color.None));
        assertFalse(PointState.Black6.canHit(Color.White));
        assertFalse(PointState.Black6.canHit(Color.Black));
        assertFalse(PointState.Black7.canHit(Color.None));
        assertFalse(PointState.Black7.canHit(Color.White));
        assertFalse(PointState.Black7.canHit(Color.Black));
        assertFalse(PointState.Black8.canHit(Color.None));
        assertFalse(PointState.Black8.canHit(Color.White));
        assertFalse(PointState.Black8.canHit(Color.Black));
        assertFalse(PointState.Black9.canHit(Color.None));
        assertFalse(PointState.Black9.canHit(Color.White));
        assertFalse(PointState.Black9.canHit(Color.Black));
        assertFalse(PointState.Black10.canHit(Color.None));
        assertFalse(PointState.Black10.canHit(Color.White));
        assertFalse(PointState.Black10.canHit(Color.Black));
        assertFalse(PointState.Black11.canHit(Color.None));
        assertFalse(PointState.Black11.canHit(Color.White));
        assertFalse(PointState.Black11.canHit(Color.Black));
        assertFalse(PointState.Black12.canHit(Color.None));
        assertFalse(PointState.Black12.canHit(Color.White));
        assertFalse(PointState.Black12.canHit(Color.Black));
        assertFalse(PointState.Black13.canHit(Color.None));
        assertFalse(PointState.Black13.canHit(Color.White));
        assertFalse(PointState.Black13.canHit(Color.Black));
        assertFalse(PointState.Black14.canHit(Color.None));
        assertFalse(PointState.Black14.canHit(Color.White));
        assertFalse(PointState.Black14.canHit(Color.Black));
        assertFalse(PointState.Black15.canHit(Color.None));
        assertFalse(PointState.Black15.canHit(Color.White));
        assertFalse(PointState.Black15.canHit(Color.Black));
    }

    @Test
    public void isAvailable() {
        assertTrue(PointState.Empty.isAvailable(Color.White));

        assertTrue(PointState.White1.isAvailable(Color.White));
        assertTrue(PointState.White2.isAvailable(Color.White));
        assertTrue(PointState.White3.isAvailable(Color.White));
        assertTrue(PointState.White4.isAvailable(Color.White));
        assertTrue(PointState.White5.isAvailable(Color.White));
        assertTrue(PointState.White6.isAvailable(Color.White));
        assertTrue(PointState.White7.isAvailable(Color.White));
        assertTrue(PointState.White8.isAvailable(Color.White));
        assertTrue(PointState.White9.isAvailable(Color.White));
        assertTrue(PointState.White10.isAvailable(Color.White));
        assertTrue(PointState.White11.isAvailable(Color.White));
        assertTrue(PointState.White12.isAvailable(Color.White));
        assertTrue(PointState.White13.isAvailable(Color.White));
        assertTrue(PointState.White14.isAvailable(Color.White));
        assertTrue(PointState.White15.isAvailable(Color.White));

        assertTrue(PointState.Black1.isAvailable(Color.White));
        assertFalse(PointState.Black2.isAvailable(Color.White));
        assertFalse(PointState.Black3.isAvailable(Color.White));
        assertFalse(PointState.Black4.isAvailable(Color.White));
        assertFalse(PointState.Black5.isAvailable(Color.White));
        assertFalse(PointState.Black6.isAvailable(Color.White));
        assertFalse(PointState.Black7.isAvailable(Color.White));
        assertFalse(PointState.Black8.isAvailable(Color.White));
        assertFalse(PointState.Black9.isAvailable(Color.White));
        assertFalse(PointState.Black10.isAvailable(Color.White));
        assertFalse(PointState.Black11.isAvailable(Color.White));
        assertFalse(PointState.Black12.isAvailable(Color.White));
        assertFalse(PointState.Black13.isAvailable(Color.White));
        assertFalse(PointState.Black14.isAvailable(Color.White));
        assertFalse(PointState.Black15.isAvailable(Color.White));

        assertTrue(PointState.Empty.isAvailable(Color.Black));

        assertTrue(PointState.White1.isAvailable(Color.Black));
        assertFalse(PointState.White2.isAvailable(Color.Black));
        assertFalse(PointState.White3.isAvailable(Color.Black));
        assertFalse(PointState.White4.isAvailable(Color.Black));
        assertFalse(PointState.White5.isAvailable(Color.Black));
        assertFalse(PointState.White6.isAvailable(Color.Black));
        assertFalse(PointState.White7.isAvailable(Color.Black));
        assertFalse(PointState.White8.isAvailable(Color.Black));
        assertFalse(PointState.White9.isAvailable(Color.Black));
        assertFalse(PointState.White10.isAvailable(Color.Black));
        assertFalse(PointState.White11.isAvailable(Color.Black));
        assertFalse(PointState.White12.isAvailable(Color.Black));
        assertFalse(PointState.White13.isAvailable(Color.Black));
        assertFalse(PointState.White14.isAvailable(Color.Black));
        assertFalse(PointState.White15.isAvailable(Color.Black));

        assertTrue(PointState.Black1.isAvailable(Color.Black));
        assertTrue(PointState.Black2.isAvailable(Color.Black));
        assertTrue(PointState.Black3.isAvailable(Color.Black));
        assertTrue(PointState.Black4.isAvailable(Color.Black));
        assertTrue(PointState.Black5.isAvailable(Color.Black));
        assertTrue(PointState.Black6.isAvailable(Color.Black));
        assertTrue(PointState.Black7.isAvailable(Color.Black));
        assertTrue(PointState.Black8.isAvailable(Color.Black));
        assertTrue(PointState.Black9.isAvailable(Color.Black));
        assertTrue(PointState.Black10.isAvailable(Color.Black));
        assertTrue(PointState.Black11.isAvailable(Color.Black));
        assertTrue(PointState.Black12.isAvailable(Color.Black));
        assertTrue(PointState.Black13.isAvailable(Color.Black));
        assertTrue(PointState.Black14.isAvailable(Color.Black));
        assertTrue(PointState.Black15.isAvailable(Color.Black));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert1() {
        PointState.Empty.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert2() {
        PointState.White1.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert3() {
        PointState.White2.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert4() {
        PointState.White3.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert5() {
        PointState.White4.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert6() {
        PointState.White5.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert7() {
        PointState.White6.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert8() {
        PointState.White7.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert9() {
        PointState.White8.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert10() {
        PointState.White9.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert11() {
        PointState.White10.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert12() {
        PointState.White11.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert13() {
        PointState.White12.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert14() {
        PointState.White13.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert15() {
        PointState.White14.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert16() {
        PointState.White15.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert17() {
        PointState.Black1.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert18() {
        PointState.Black2.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert19() {
        PointState.Black3.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert20() {
        PointState.Black4.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert21() {
        PointState.Black5.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert22() {
        PointState.Black6.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert23() {
        PointState.Black7.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert24() {
        PointState.Black8.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert25() {
        PointState.Black9.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert26() {
        PointState.Black10.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert27() {
        PointState.Black11.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert28() {
        PointState.Black12.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert29() {
        PointState.Black13.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert30() {
        PointState.Black14.isAvailable(Color.None);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void isAvailable_assert31() {
        PointState.Black15.isAvailable(Color.None);
    }

    @Test
    public void getQuantity() {
        assertEquals(PointState.Empty.getQuantity(), 0);
        assertEquals(PointState.White1.getQuantity(), 1);
        assertEquals(PointState.White2.getQuantity(), 2);
        assertEquals(PointState.White3.getQuantity(), 3);
        assertEquals(PointState.White4.getQuantity(), 4);
        assertEquals(PointState.White5.getQuantity(), 5);
        assertEquals(PointState.White6.getQuantity(), 6);
        assertEquals(PointState.White7.getQuantity(), 7);
        assertEquals(PointState.White8.getQuantity(), 8);
        assertEquals(PointState.White9.getQuantity(), 9);
        assertEquals(PointState.White10.getQuantity(), 10);
        assertEquals(PointState.White11.getQuantity(), 11);
        assertEquals(PointState.White12.getQuantity(), 12);
        assertEquals(PointState.White13.getQuantity(), 13);
        assertEquals(PointState.White14.getQuantity(), 14);
        assertEquals(PointState.White15.getQuantity(), 15);
        assertEquals(PointState.Black1.getQuantity(), 1);
        assertEquals(PointState.Black2.getQuantity(), 2);
        assertEquals(PointState.Black3.getQuantity(), 3);
        assertEquals(PointState.Black4.getQuantity(), 4);
        assertEquals(PointState.Black5.getQuantity(), 5);
        assertEquals(PointState.Black6.getQuantity(), 6);
        assertEquals(PointState.Black7.getQuantity(), 7);
        assertEquals(PointState.Black8.getQuantity(), 8);
        assertEquals(PointState.Black9.getQuantity(), 9);
        assertEquals(PointState.Black10.getQuantity(), 10);
        assertEquals(PointState.Black11.getQuantity(), 11);
        assertEquals(PointState.Black12.getQuantity(), 12);
        assertEquals(PointState.Black13.getQuantity(), 13);
        assertEquals(PointState.Black14.getQuantity(), 14);
        assertEquals(PointState.Black15.getQuantity(), 15);
    }
}
