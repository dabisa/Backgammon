package com.dkelava.bglib.model;

import org.junit.Test;

public class MoveNodeTest {

    /*
    class Move {
        public Point point;
        public DieFace die;
    }

    private void test_moves(BoardConfiguration boardConfiguration, DieFace[] dice, Move[][] moves) {
        Board board = BoardConfiguration.Initial.create();

        Collection<DieFace> dice = Arrays.asList( DieFace.Die1, DieFace.Die1, DieFace.Die1, DieFace.Die1 );

        MoveNode moveTree = MoveNode.createCustom(board, Color.White, dice);

        assertNotNull(moveTree);
        assertTrue(moveTree.isStart());
        assertFalse(moveTree.isEnd());

        moveTree.find();
        moveTree.getDiceState();
        moveTree.getMoves();
        moveTree.getPrevious();
        moveTree.isMovable();
        moveTree.isMovable(s, d);

    }
    */

    @Test
    public void initial_1_1() {
    }
}
