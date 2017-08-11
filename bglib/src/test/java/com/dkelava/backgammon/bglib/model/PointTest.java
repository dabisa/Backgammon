package com.dkelava.backgammon.bglib.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointTest {

    @Test
    public void getBar() {
        assertEquals(Point.getBar(Color.White), Point.WhiteBar);
        assertEquals(Point.getBar(Color.Black), Point.BlackBar);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getBar_assert() {
        Point.getBar(Color.None);
    }

    @Test
    public void getHome() {
        assertEquals(Point.getHome(Color.White), Point.WhiteHome);
        assertEquals(Point.getHome(Color.Black), Point.BlackHome);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getHome_assert() {
        Point.getHome(Color.None);
    }

    @Test
    public void getPoint() {
        assertEquals(Point.getPoint(Color.White, 0), Point.WhiteBar);
        assertEquals(Point.getPoint(Color.White, 1), Point.Point1);
        assertEquals(Point.getPoint(Color.White, 2), Point.Point2);
        assertEquals(Point.getPoint(Color.White, 3), Point.Point3);
        assertEquals(Point.getPoint(Color.White, 4), Point.Point4);
        assertEquals(Point.getPoint(Color.White, 5), Point.Point5);
        assertEquals(Point.getPoint(Color.White, 6), Point.Point6);
        assertEquals(Point.getPoint(Color.White, 7), Point.Point7);
        assertEquals(Point.getPoint(Color.White, 8), Point.Point8);
        assertEquals(Point.getPoint(Color.White, 9), Point.Point9);
        assertEquals(Point.getPoint(Color.White, 10), Point.Point10);
        assertEquals(Point.getPoint(Color.White, 11), Point.Point11);
        assertEquals(Point.getPoint(Color.White, 12), Point.Point12);
        assertEquals(Point.getPoint(Color.White, 13), Point.Point13);
        assertEquals(Point.getPoint(Color.White, 14), Point.Point14);
        assertEquals(Point.getPoint(Color.White, 15), Point.Point15);
        assertEquals(Point.getPoint(Color.White, 16), Point.Point16);
        assertEquals(Point.getPoint(Color.White, 17), Point.Point17);
        assertEquals(Point.getPoint(Color.White, 18), Point.Point18);
        assertEquals(Point.getPoint(Color.White, 19), Point.Point19);
        assertEquals(Point.getPoint(Color.White, 20), Point.Point20);
        assertEquals(Point.getPoint(Color.White, 21), Point.Point21);
        assertEquals(Point.getPoint(Color.White, 22), Point.Point22);
        assertEquals(Point.getPoint(Color.White, 23), Point.Point23);
        assertEquals(Point.getPoint(Color.White, 24), Point.Point24);
        assertEquals(Point.getPoint(Color.White, 25), Point.WhiteHome);

        assertEquals(Point.getPoint(Color.Black, 0), Point.BlackBar);
        assertEquals(Point.getPoint(Color.Black, 1), Point.Point24);
        assertEquals(Point.getPoint(Color.Black, 2), Point.Point23);
        assertEquals(Point.getPoint(Color.Black, 3), Point.Point22);
        assertEquals(Point.getPoint(Color.Black, 4), Point.Point21);
        assertEquals(Point.getPoint(Color.Black, 5), Point.Point20);
        assertEquals(Point.getPoint(Color.Black, 6), Point.Point19);
        assertEquals(Point.getPoint(Color.Black, 7), Point.Point18);
        assertEquals(Point.getPoint(Color.Black, 8), Point.Point17);
        assertEquals(Point.getPoint(Color.Black, 9), Point.Point16);
        assertEquals(Point.getPoint(Color.Black, 10), Point.Point15);
        assertEquals(Point.getPoint(Color.Black, 11), Point.Point14);
        assertEquals(Point.getPoint(Color.Black, 12), Point.Point13);
        assertEquals(Point.getPoint(Color.Black, 13), Point.Point12);
        assertEquals(Point.getPoint(Color.Black, 14), Point.Point11);
        assertEquals(Point.getPoint(Color.Black, 15), Point.Point10);
        assertEquals(Point.getPoint(Color.Black, 16), Point.Point9);
        assertEquals(Point.getPoint(Color.Black, 17), Point.Point8);
        assertEquals(Point.getPoint(Color.Black, 18), Point.Point7);
        assertEquals(Point.getPoint(Color.Black, 19), Point.Point6);
        assertEquals(Point.getPoint(Color.Black, 20), Point.Point5);
        assertEquals(Point.getPoint(Color.Black, 21), Point.Point4);
        assertEquals(Point.getPoint(Color.Black, 22), Point.Point3);
        assertEquals(Point.getPoint(Color.Black, 23), Point.Point2);
        assertEquals(Point.getPoint(Color.Black, 24), Point.Point1);
        assertEquals(Point.getPoint(Color.Black, 25), Point.BlackHome);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getPoint_assert1() {
        Point.getPoint(Color.None, 0);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getPoint_assert2() {
        Point.getPoint(Color.None, -1);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getPoint_assert3() {
        Point.getPoint(Color.None, 25);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getPoint_assert4() {
        Point.getPoint(Color.White, -1);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getPoint_assert5() {
        Point.getPoint(Color.White, 26);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getPoint_assert6() {
        Point.getPoint(Color.Black, -1);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getPoint_assert7() {
        Point.getPoint(Color.Black, 26);
    }

    @Test
    public void advance() {
        assertEquals(Point.WhiteBar.advance(Color.White, 1), Point.Point1);
        assertEquals(Point.Point1.advance(Color.White, 23), Point.Point24);
        assertEquals(Point.Point24.advance(Color.White, 1), Point.WhiteHome);
        assertEquals(Point.Point20.advance(Color.White, 10), Point.WhiteHome);

        assertEquals(Point.BlackBar.advance(Color.Black, 1), Point.Point24);
        assertEquals(Point.Point24.advance(Color.Black, 23), Point.Point1);
        assertEquals(Point.Point1.advance(Color.Black, 1), Point.BlackHome);
        assertEquals(Point.Point5.advance(Color.Black, 10), Point.BlackHome);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void advance_assert1() {
        Point.Point1.advance(Color.None, 1);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void advance_assert2() {
        Point.WhiteBar.advance(Color.Black, 1);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void advance_assert3() {
        Point.WhiteHome.advance(Color.Black, 1);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void advance_assert4() {
        Point.BlackBar.advance(Color.White, 1);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void advance_assert5() {
        Point.BlackHome.advance(Color.White, 1);
    }

    @Test
    public void distance() {
        assertEquals(Point.WhiteBar.distance(Point.WhiteHome), 25);
        assertEquals(Point.WhiteHome.distance(Point.WhiteBar), 25);

        assertEquals(Point.BlackBar.distance(Point.BlackHome), 25);
        assertEquals(Point.BlackHome.distance(Point.BlackBar), 25);

    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void distance_assert1() {
        Point.WhiteBar.distance(Point.BlackBar);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void distance_assert2() {
        Point.WhiteBar.distance(Point.BlackHome);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void distance_assert3() {
        Point.WhiteHome.distance(Point.BlackBar);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void distance_assert4() {
        Point.WhiteHome.distance(Point.BlackHome);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void distance_assert5() {
        Point.BlackBar.distance(Point.WhiteBar);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void distance_assert6() {
        Point.BlackBar.distance(Point.WhiteHome);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void distance_assert7() {
        Point.BlackHome.distance(Point.WhiteBar);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void distance_assert8() {
        Point.BlackHome.distance(Point.WhiteHome);
    }

    @Test
    public void getPoints() {
        final Point[] whitePoints = {Point.WhiteBar, Point.Point1, Point.Point2, Point.Point3, Point.Point4, Point.Point5, Point.Point6, Point.Point7, Point.Point8, Point.Point9, Point.Point10, Point.Point11, Point.Point12, Point.Point13, Point.Point14, Point.Point15, Point.Point16, Point.Point17, Point.Point18, Point.Point19, Point.Point20, Point.Point21, Point.Point22, Point.Point23, Point.Point24, Point.WhiteHome};
        final Point[] whitePoints2 = Point.getPoints(Color.White);

        for (int i = 0; i < whitePoints.length; ++i) {
            assertEquals(whitePoints[i], whitePoints2[i]);
        }

        final Point[] blackPoints = {Point.BlackBar, Point.Point24, Point.Point23, Point.Point22, Point.Point21, Point.Point20, Point.Point19, Point.Point18, Point.Point17, Point.Point16, Point.Point15, Point.Point14, Point.Point13, Point.Point12, Point.Point11, Point.Point10, Point.Point9, Point.Point8, Point.Point7, Point.Point6, Point.Point5, Point.Point4, Point.Point3, Point.Point2, Point.Point1, Point.BlackHome};
        final Point[] blackPoints2 = Point.getPoints(Color.Black);

        for (int i = 0; i < blackPoints.length; ++i) {
            assertEquals(blackPoints[i], blackPoints2[i]);
        }
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getPoints_assert() {
        Point.getPoints(Color.None);
    }
}
