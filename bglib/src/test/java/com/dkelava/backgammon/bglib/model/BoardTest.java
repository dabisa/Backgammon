package com.dkelava.backgammon.bglib.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    private final String empty   = "N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00";
    private final String initial = "N00N00W02N00N00N00N00B05N00B03N00N00N00W05B05N00N00N00W03N00W05N00N00N00N00B02N00N00";
    private final String whiteBearingOff = "N00N00N00N00N00N00N00N00B15N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00N00N00W14N00";
    private final String blackBearingOff = "N00N00N00N00N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00W15N00N00N00N00N00N00N00B14";

    private final String bearingOff1 = "N00N00N00N00N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00N00N00W14B14";
    private final String BothBearOff1 = "N00B13B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13N00";

    private final String WhiteWins1 = "N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B15W15N00";
    private final String BlackWins1 = "N00N00W15N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B15";
    private final String Invalid1 = "B15N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00W15N00";
    private final String Invalid2 = "W15N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B15N00";
    private final String Invalid3 = "N00B15N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00W15";
    private final String Invalid4 = "N00W15N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B15";
    private final String Test1 = "N00B01B01N00N00N00N00W04N00W03B01N00N00B05W05N00N00W01B03N00B04N00N00N00N00W01W01N00";

    
    private Board board = null;

    @Before
    public void create() {
        board = new Board();
    }

    @Test
    public void constructor() {
        for (Point point : Point.values()) {
            assertEquals(board.getPoint(point), PointState.Empty);
        }
    }

    @Test
    public void copyConstructor() throws Exception {
        board.restore(initial);
        Board board2 = new Board(board);

        assertEquals(board, board2);
    }

    @Test
    public void equals() throws Exception {
        Board board2 = new Board();

        board.restore(initial);
        board2.restore(initial);
        assertEquals(board, board2);

        board.restore(initial);
        board2.restore(Test1);
        assertNotEquals(board, board2);

        board.restore(initial);
        board2.restore(initial);
        board2.addChecker(Point.Point2, Color.White);
        assertNotEquals(board, board2);
        board2.removeChecker(Point.Point2, Color.White);
        assertEquals(board, board2);
        board.setPoint(Point.Point1, PointState.White15);
        assertNotEquals(board, board2);
        board2.setPoint(Point.Point1, PointState.White15);
        assertEquals(board, board2);
    }

    @Test
    public void clear() {
        for (Point point : Point.values()) {
            board.setPoint(point, PointState.White1);
        }

        board.clear();

        for (Point point : Point.values()) {
            assertEquals(board.getPoint(point), PointState.Empty);
        }
    }

    @Test
    public void initialize() throws Exception {
        board.clear();
        board.initialize();
        Board board2 = Board.decode(initial);
        for (Point point : Point.values()) {
            assertEquals(board.getPoint(point), board2.getPoint(point));
        }
    }

    @Test
    public void getPoint_setPoint() {
        for (Point point : Point.values()) {
            for (PointState state : PointState.values()) {
                board.setPoint(point, state);
                assertEquals(board.getPoint(point), state);
            }
        }
    }

    @Test
    public void canBearOff() throws Exception {
        board.restore(empty);
        assertFalse(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));

        board.restore(initial);
        assertFalse(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));

        board.restore(whiteBearingOff);
        assertTrue(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));

        board.restore(blackBearingOff);
        assertFalse(board.canBearOff(Color.White));
        assertTrue(board.canBearOff(Color.Black));

        board.restore(bearingOff1);
        assertTrue(board.canBearOff(Color.White));
        assertTrue(board.canBearOff(Color.Black));

        board.restore(WhiteWins1);
        assertFalse(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));

        board.restore(BlackWins1);
        assertFalse(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));

        board.restore(Invalid1);
        assertFalse(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));

        board.restore(Invalid2);
        assertFalse(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));

        board.restore(Invalid3);
        assertFalse(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));

        board.restore(Invalid4);
        assertFalse(board.canBearOff(Color.White));
        assertFalse(board.canBearOff(Color.Black));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void canBearOff_assert() {
        board.canBearOff(Color.None);
    }

    @Test
    public void isLegal() throws Exception {
        board.restore(empty);
        assertFalse(board.isLegal());

        board.restore(initial);
        assertTrue(board.isLegal());

        board.restore(whiteBearingOff);
        assertTrue(board.isLegal());

        board.restore(blackBearingOff);
        assertTrue(board.isLegal());

        board.restore(BothBearOff1);
        assertTrue(board.isLegal());

        board.restore(WhiteWins1);
        assertTrue(board.isLegal());

        board.restore(BlackWins1);
        assertTrue(board.isLegal());

        board.restore(Invalid1);
        assertFalse(board.isLegal());

        board.restore(Invalid2);
        assertFalse(board.isLegal());

        board.restore(Invalid3);
        assertFalse(board.isLegal());

        board.restore(Invalid4);
        assertFalse(board.isLegal());

        board.restore(Test1);
        assertTrue(board.isLegal());
    }

    @Test
    public void addChecker_removeChecker() {
        board.clear();
        assertEquals(board.getPoint(Point.Point1), PointState.Empty);
        board.addChecker(Point.Point1, Color.White);
        assertEquals(board.getPoint(Point.Point1), PointState.White1);
        board.removeChecker(Point.Point1, Color.White);
        assertEquals(board.getPoint(Point.Point1), PointState.Empty);

        board.clear();
        assertEquals(board.getPoint(Point.Point1), PointState.Empty);
        board.addChecker(Point.Point1, Color.Black);
        assertEquals(board.getPoint(Point.Point1), PointState.Black1);
        board.removeChecker(Point.Point1, Color.Black);
        assertEquals(board.getPoint(Point.Point1), PointState.Empty);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void addChecker_assert1() {
        board.clear();
        board.setPoint(Point.Point1, PointState.White15);
        board.addChecker(Point.Point1, Color.White);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void addChecker_assert2() {
        board.clear();
        board.setPoint(Point.Point1, PointState.Black15);
        board.addChecker(Point.Point1, Color.Black);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void removeChecker_assert1() {
        board.clear();
        board.setPoint(Point.Point1, PointState.Empty);
        board.removeChecker(Point.Point1, Color.White);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void removeChecker_assert2() {
        board.clear();
        board.setPoint(Point.Point1, PointState.Empty);
        board.removeChecker(Point.Point1, Color.Black);
    }

}
