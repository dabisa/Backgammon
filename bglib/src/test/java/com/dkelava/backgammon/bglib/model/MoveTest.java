package com.dkelava.backgammon.bglib.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MoveTest {

    private void testValidMove(String before, String after, Color color, Point source, Point destination, Die.Face die, boolean isHit, int distance) throws Exception {
        Board board = Board.decode(before);
        Board boardBefore = Board.decode(before);
        Board boardAfter = Board.decode(after);

        assertTrue(Move.isMovable(board, color, source, die));

        Move move = Move.createMove(board, color, source, die);

        assertNotNull(move);
        assertEquals(move.getColor(), color);
        assertEquals(move.getSource(), source);
        assertEquals(move.getDestination(), destination);
        assertEquals(move.getDie(), die);
        assertEquals(move.isHit(), isHit);
        assertEquals(move.getDistance(), distance);
        assertTrue(move.canExecute(board));

        move.execute(board);

        assertTrue(board.equals(boardAfter));
        assertTrue(move.canRollback(board));

        move.rollback(board);

        assertTrue(board.equals(boardBefore));
    }

    private void testInvalidMove(String before, Color color, Point source, Die.Face die) throws Exception {
        Board board = Board.decode(before);
        assertFalse(Move.isMovable(board, color, source, die));
    }

    @Test
    public void white_moves_w1_to_empty() throws Exception {
        String before = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        String after  = "N00B06N00N00W02W01B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testValidMove(before, after, Color.White, Point.Point2, Point.Point4, Die.Face.Die2, false, 2);
    }

    @Test
    public void white_moves_w1_to_w1() throws Exception {
        String before = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        String after  = "N00B06N00N00W02N00B01B02W02W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testValidMove(before, after, Color.White, Point.Point2, Point.Point7, Die.Face.Die5, false, 5);
    }

    @Test
    public void white_moves_w1_to_w2() throws Exception {
        String before = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        String after  = "N00B06N00N00W03N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testValidMove(before, after, Color.White, Point.Point2, Point.Point3, Die.Face.Die1, false, 1);
    }

    @Test
    public void white_moves_w1_to_b1() throws Exception {
        String before = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        String after  = "N00B07N00N00W02N00W01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testValidMove(before, after, Color.White, Point.Point2, Point.Point5, Die.Face.Die3, true, 3);
    }

    @Test
    public void white_moves_w2_to_empty() throws Exception {
        String before = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        String after  = "N00B06N00W01W01W01B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testValidMove(before, after, Color.White, Point.Point3, Point.Point4, Die.Face.Die1, false, 1);
    }

    @Test
    public void white_moves_w2_to_w1() throws Exception {
        String before = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        String after  = "N00B06N00W01W01N00B01B02W02W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testValidMove(before, after, Color.White, Point.Point3, Point.Point7, Die.Face.Die4, false, 4);
    }

    @Test
    public void white_moves_w2_to_w2() throws Exception {
        String before = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        String after  = "N00B06N00W01W01N00B01B02W01W03N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testValidMove(before, after, Color.White, Point.Point3, Point.Point8, Die.Face.Die5, false, 5);
    }

    @Test
    public void white_moves_w2_to_b1() throws Exception {
        String before = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        String after  = "N00B07N00W01W01N00W01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testValidMove(before, after, Color.White, Point.Point3, Point.Point5, Die.Face.Die2, true, 2);
    }

    @Test
    public void white_bears_off() throws Exception {
        final String before = "N00N00N00N00N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00N00N00W14B14";
        final String after  = "N00N00N00N00N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00W15B14";
        testValidMove(before, after, Color.White, Point.Point19, Point.WhiteHome, Die.Face.Die6, false, 6);
    }

    @Test
    public void white_bears_off2() throws Exception {
        final String before = "N00B13B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13N00";
        final String after  = "N00B13B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00N00W14N00";
        testValidMove(before, after, Color.White, Point.Point24, Point.WhiteHome, Die.Face.Die1, false, 1);
    }

    @Test
    public void white_bears_off_short() throws Exception {
        final String before = "N00B13B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13N00";
        final String after  = "N00B13B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01W14N00";
        testValidMove(before, after, Color.White, Point.Point20, Point.WhiteHome, Die.Face.Die6, false, 5);
    }

    @Test
    public void white_enters() throws Exception {
        final String before = "W01B13N00W01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B01N00W13B01";
        final String after  = "N00B13W01W01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B01N00W13B01";
        testValidMove(before, after, Color.White, Point.WhiteBar, Point.Point1, Die.Face.Die1, false, 1);
    }

    @Test
    public void invalid_white_moving_empty() throws Exception {
        final String initial = "N00N00W02N00N00N00N00B05N00B03N00N00N00W05B05N00N00N00W03N00W05N00N00N00N00B02N00N00";
        testInvalidMove(initial, Color.White, Point.Point2, Die.Face.Die1);
    }

    @Test
    public void invalid_white_moving_black() throws Exception {
        final String initial = "N00N00W02N00N00N00N00B05N00B03N00N00N00W05B05N00N00N00W03N00W05N00N00N00N00B02N00N00";
        testInvalidMove(initial, Color.White, Point.Point6, Die.Face.Die1);
    }

    @Test
    public void invalid_white_blocked() throws Exception {
        final String moveTest = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testInvalidMove(moveTest, Color.White, Point.Point2, Die.Face.Die4);
    }

    @Test
    public void invalid_white_bearing_off_not_allowed() throws Exception {
        final String moveTest = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testInvalidMove(moveTest, Color.White, Point.Point20, Die.Face.Die5);
    }

    @Test
    public void invalid_white_bar_not_empty() throws Exception {
        final String captured = "W01B13N00W01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B01N00W13B01";
        testInvalidMove(captured, Color.White, Point.Point20, Die.Face.Die5);
    }

    @Test
    public void invalid_white_bearing_off_not_optimal() throws Exception {
        final String bearingOff = "N00B13B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13N00";
        testInvalidMove(bearingOff, Color.White, Point.Point24, Die.Face.Die2);
    }

    @Test
    public void black_moves_b1_to_empty() throws Exception {
        final String before = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06B06";
        final String after  = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01B01B02N00N00W06B06";
        testValidMove(before, after, Color.Black, Point.Point23, Point.Point21, Die.Face.Die2, false, 2);
    }

    @Test
    public void black_moves_b1_to_b1() throws Exception {
        final String before = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06B06";
        final String after  = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B02W02W01N00B02N00N00W06B06";
        testValidMove(before, after, Color.Black, Point.Point23, Point.Point18, Die.Face.Die5, false, 5);
    }

    @Test
    public void black_moves_b1_to_b2() throws Exception {
        final String before = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06B06";
        final String after  = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B03N00N00W06B06";
        testValidMove(before, after, Color.Black, Point.Point23, Point.Point22, Die.Face.Die1, false, 1);
    }

    @Test
    public void black_moves_b1_to_o1() throws Exception {
        final String before = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06B06";
        final String after  = "W01N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02B01N00B02N00N00W06B06";
        testValidMove(before, after, Color.Black, Point.Point23, Point.Point20, Die.Face.Die3, true, 3);
    }

    @Test
    public void black_moves_b2_to_empty() throws Exception {
        final String before = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06B06";
        final String after  = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01B01B01B01N00W06B06";
        testValidMove(before, after, Color.Black, Point.Point22, Point.Point21, Die.Face.Die1, false, 1);
    }

    @Test
    public void black_moves_b2_to_b1() throws Exception {
        final String before = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06B06";
        final String after  = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B02W02W01N00B01B01N00W06B06";
        testValidMove(before, after, Color.Black, Point.Point22, Point.Point18, Die.Face.Die4, false, 4);
    }

    @Test
    public void black_moves_b2_to_b2() throws Exception {
        final String before = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06B06";
        final String after  = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B03B01W02W01N00B01B01N00W06B06";
        testValidMove(before, after, Color.Black, Point.Point22, Point.Point17, Die.Face.Die5, false, 5);
    }

    @Test
    public void black_moves_b2_to_o1() throws Exception {
        final String before = "N00N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06B06";
        final String after  = "W01N00N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02B01N00B01B01N00W06B06";
        testValidMove(before, after, Color.Black, Point.Point22, Point.Point20, Die.Face.Die2, true, 2);
    }

    @Test
    public void black_bears_off() throws Exception {
        final String before = "N00N00N00N00N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00N00N00W14B14";
        final String after  = "N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00N00N00W14B15";
        testValidMove(before, after, Color.Black, Point.Point6, Point.BlackHome, Die.Face.Die6, false, 6);
    }

    @Test
    public void black_bears_off2() throws Exception {
        final String before = "N00N00B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13B13";
        final String after = "N00N00N00N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13B14";
        testValidMove(before, after, Color.Black, Point.Point1, Point.BlackHome, Die.Face.Die1, false, 1);
    }

    @Test
    public void black_bears_off_short() throws Exception {
        final String before = "N00N00B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13B13";
        final String after = "N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13B14";
        testValidMove(before, after, Color.Black, Point.Point5, Point.BlackHome, Die.Face.Die6, false, 5);
    }

    @Test
    public void black_enters() throws Exception {
        final String before = "W01B01N00W01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B01N00W13B13";
        final String after  = "W01N00N00W01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B01B01W13B13";
        testValidMove(before, after, Color.Black, Point.BlackBar, Point.Point24, Die.Face.Die1, false, 1);
    }

    @Test
    public void invalid_black_moving_empty() throws Exception {
        final String initial = "N00N00W02N00N00N00N00B05N00B03N00N00N00W05B05N00N00N00W03N00W05N00N00N00N00B02N00N00";
        testInvalidMove(initial, Color.Black, Point.Point2, Die.Face.Die1);
    }

    @Test
    public void invalid_black_moving_white() throws Exception {
        final String initial = "N00N00W02N00N00N00N00B05N00B03N00N00N00W05B05N00N00N00W03N00W05N00N00N00N00B02N00N00";
        testInvalidMove(initial, Color.Black, Point.Point1, Die.Face.Die1);
    }

    @Test
    public void invalid_black_blocked() throws Exception {
        final String moveTest = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testInvalidMove(moveTest, Color.Black, Point.Point2, Die.Face.Die4);
    }

    @Test
    public void invalid_black_bearing_off_not_allowed() throws Exception {
        final String moveTest = "N00B06N00W01W02N00B01B02W01W02N00N00N00N00N00N00N00N00B02B01W02W01N00B02B01N00W06N00";
        testInvalidMove(moveTest, Color.Black, Point.Point20, Die.Face.Die5);
    }

    @Test
    public void invalid_black_bar_not_empty() throws Exception {
        final String captured = "W01B13N00W01N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00B01N00W13B01";
        testInvalidMove(captured, Color.Black, Point.Point20, Die.Face.Die5);
    }

    @Test
    public void invalid_black_bearing_off_not_optimal() throws Exception {
        final String bearingOff = "N00B13B01N00N00N00B01N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00N00N00W01W13N00";
        testInvalidMove(bearingOff, Color.Black, Point.Point24, Die.Face.Die2);
    }
}
