package com.dkelava.bglib.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BackgammonTest {

    private void testAllMoves(Backgammon backgammon) throws Exception
    {
        for (Point source : Point.getPoints(backgammon.getState().getCurrentPlayer())) {
            if (backgammon.getState().getMoves().isMovable(source)) {
                for (Point destination : Point.getPoints(backgammon.getState().getCurrentPlayer())) {
                    if (backgammon.getState().getMoves().isMovable(source, destination)) {
                        testMove(backgammon, source, destination);
                    }
                }
            }
        }
    }

    private void testMove(Backgammon backgammon, Point source, Point destination) throws Exception {
        String before = backgammon.encode();
        backgammon.move(source, destination);
        backgammon.undoMove(source, destination);
        String after = backgammon.encode();
        if(!before.equals(after)) {
            System.err.print("source: " + source.encode());
            System.err.print("destination: " + destination.encode());
        }
        assertEquals(before, after);
    }
/*
    @Test
    public void test() throws Exception {
        String state = "MBNN110001N00-N00-B02-B04-B02-W01-N00-B05-N00-N00-N00-B02-N00-N00-W01-N00-N00-N00-N00-N00-W05-W01-N00-W02-N00-W05-N00-N00-";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        testAllMoves(backgammon);
    }

    @Test
    public void test2() throws Exception {
        String state = "MWNN330001N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-B11-N00-W01-B02-W03-B02-W11-N00-";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        testAllMoves(backgammon);
    }

    @Test
    public void test3() throws Exception {
        String state = "NWNN610001N00-N00-B02-N00-B01-B02-N00-N00-B01-B02-N00-B01-N00-B01-B02-N00-N00-N00-N00-B01-N00-N00-N00-W01-W01-B02-W13-N00-";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die6), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 1);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die6), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 1);
    }

    @Test
    public void test4() throws Exception {
        String state = "NWNN160001N00-N00-B02-N00-B01-B02-N00-N00-B01-B02-N00-B01-N00-B01-B02-N00-N00-N00-N00-B01-N00-N00-N00-W01-W01-B02-W13-N00-";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die6), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 1);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die6), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 1);
    }

    @Test
    public void test5() throws Exception {
        String state = "NWNN320001N00-N00-B07-B05-B02-B01-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-W01-W02-W10-W02-N00-";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die2), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 1);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die2), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 1);
    }

    @Test
    public void test6() throws Exception {
        String state = "MBNN340001N00-N00-B02-W03-B01-N00-N00-B10-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-W01-W02-W01-N00-W08-N00-N00-N00-B02-MB033";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 0);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die4), 1);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 0);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die4), 1);
    }


    @Test
    public void test7() throws Exception {
        String state = "MWNN130001N00-N00-B05-B03-N00-N00-B01-N00-B01-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-W01-N00-N00-B03-B02-W08-W06-N00-";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 1);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 1);
    }

    @Test
    public void test8() throws Exception {
        String state = "NBNN120001N00-N00-B01-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-W10-W02-W03-B14-MB021MB012";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 0);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die2), 0);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 0);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die2), 0);
    }

    @Test
    public void test9() throws Exception {
        String state = "NWNN310001N00-N00-B01-B01-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-W06-W01-W08-B13-MW221MW233";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 0);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 0);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 0);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 0);
    }

    @Test
    public void test10() throws Exception {
        String state = "MWNN310001N00-N00-B01-B01-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-W07-W01-W07-B13-MW221";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 0);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 1);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 0);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 1);
    }

    @Test
    public void test11() throws Exception {
        String state = "MWNN310001N00-N00-B01-B01-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-N00-W01-W06-W01-W07-B13-";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 1);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die1), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die3), 1);
    }

    @Test
    public void test12() throws Exception {
        String state = "MWNN260001N00-N00-N00-B11-B01-N00-N00-B02-N00-N00-N00-N00-B01-N00-N00-N00-N00-N00-N00-N00-W01-N00-N00-W14-N00-N00-N00-N00-";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die2), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die6), 1);
        testAllMoves(backgammon);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die2), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die6), 1);
    }
*/

    @Test
    public void test13() throws Exception {
        String state = "MWNN250001N00N00N00B01N00N00N00B07N00N00N00N00N00N00N00N00N00N00N00N00N00N00N00W01N00W11W03B07";
        Backgammon backgammon = new Backgammon();
        backgammon.restore(state);
        //backgammon.undoMove(Point.Point2, Point.BlackHome);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die2), 1);
        assertEquals(backgammon.getState().getMoves().getDiceState().getNumberOfMoves(Die.Face.Die5), 1);
    }


/*
    private void test_sequence(BackgammonState initialState, BackgammonState finalState, ArrayList<Action> actions) {
        Backgammon backgammon = new Backgammon(initialState);
        assertNotNull(backgammon);
        assertEquals(backgammon, initialState);

        for (Action action : actions) {
            action.execute(backgammon);
        }

        assertEquals(backgammon, finalState);

        ListIterator<Action> li = actions.listIterator(actions.size());
        while (li.hasPrevious()) {
            li.previous().execute(backgammon);
        }

        assertEquals(backgammon, initialState);
    }

    @Test
    public void constructor_default() {
        BackgammonOld backgammon = new BackgammonOld();

        assertFalse(backgammon.isMovable());
        for (Point source : Point.values()) {
            assertEquals(backgammon.getPointState(source), PointState.Empty);
            assertFalse(backgammon.isMovable(source));
            for (Point destination : Point.values()) {
                assertFalse(backgammon.isMovable(source, destination));
                assertFalse(backgammon.isHit(source, destination));
            }
        }
        for (Die.Face die : Die.Face.values()) {
            assertEquals(backgammon.getNumberOfMoves(die), 0);
        }
        assertEquals(backgammon.getStatus(), Status.Empty);
        assertNull(backgammon.getDieOne());
        assertNull(backgammon.getDieTwo());
        assertEquals(backgammon.getCurrentPlayer(), Color.None);
        assertEquals(backgammon.getCubeOwner(), Color.None);
        assertEquals(backgammon.getWinner(), Color.None);
        assertEquals(backgammon.getStake(), 0);
    }



    @Test
    public void constructor_copy() {
        // TODO: 14.2.2017.
    }

    @Test
    public void restore() {
        // TODO: 14.2.2017.
    }

    @Test
    public void clear() {
        BackgammonOld backgammon = new BackgammonOld();
        backgammon.initialize();
        backgammon.clear();

        assertFalse(backgammon.isMovable());
        for (Point source : Point.values()) {
            assertEquals(backgammon.getPointState(source), PointState.Empty);
            assertFalse(backgammon.isMovable(source));
            for (Point destination : Point.values()) {
                assertFalse(backgammon.isMovable(source, destination));
                assertFalse(backgammon.isHit(source, destination));
            }
        }
        for (DieFace die : DieFace.values()) {
            assertEquals(backgammon.getNumberOfMoves(die), 0);
        }
        assertEquals(backgammon.getStatus(), Status.Empty);
        assertNull(backgammon.getDieOne());
        assertNull(backgammon.getDieTwo());
        assertEquals(backgammon.getCurrentPlayer(), Color.None);
        assertEquals(backgammon.getCubeOwner(), Color.None);
        assertEquals(backgammon.getWinner(), Color.None);
        assertEquals(backgammon.getStake(), 0);
    }

    @Test
    public void initialize() {
        BackgammonOld backgammon = new BackgammonOld();
        backgammon.initialize();

        assertFalse(backgammon.isMovable());
        for (Point source : Point.values()) {
            assertEquals(backgammon.getPointState(source), BoardConfiguration.Initial.get(source));
            assertFalse(backgammon.isMovable(source));
            for (Point destination : Point.values()) {
                assertFalse(backgammon.isMovable(source, destination));
                assertFalse(backgammon.isHit(source, destination));
            }
        }
        for (DieFace die : DieFace.values()) {
            assertEquals(backgammon.getNumberOfMoves(die), 0);
        }
        assertEquals(backgammon.getStatus(), Status.Start);
        assertNull(backgammon.getDieOne());
        assertNull(backgammon.getDieTwo());
        assertEquals(backgammon.getCurrentPlayer(), Color.White);
        assertEquals(backgammon.getCubeOwner(), Color.None);
        assertEquals(backgammon.getWinner(), Color.None);
        assertEquals(backgammon.getStake(), 1);
    }

    @Test
    public void initialRoll() {
        // TODO: 14.2.2017.
    }

    @Test
    public void reverseInitialRoll() {
        // TODO: 14.2.2017.
    }

    @Test
    public void roll() {
        // TODO: 14.2.2017.
    }

    @Test
    public void reverseRoll() {
        // TODO: 14.2.2017.
    }

    @Test
    public void move() {
        // TODO: 14.2.2017.
    }

    @Test
    public void reverseMove() {
        // TODO: 14.2.2017.
    }

    @Test
    public void pickUpDice() {
        // TODO: 14.2.2017.
    }

    @Test
    public void reversePickUpDice() {
        // TODO: 14.2.2017.
    }

    @Test
    public void doubleStake_and_reverse() {
        BackgammonOld backgammon = new BackgammonOld();
        assertEquals(backgammon.getStake(), 0);

        backgammon.initialize();
        assertEquals(backgammon.getStake(), 1);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.initialRoll(DieFace.Die2);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.initialRoll(DieFace.Die1);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.move(Point.Point1, Point.Point2);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.move(Point.Point1, Point.Point3);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.pickUpDice();

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 2);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 1);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 2);

        backgammon.roll(DieFace.Die1, DieFace.Die2);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 2);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 2);

        backgammon.move(Point.Point24, Point.Point23);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 2);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 2);

        backgammon.move(Point.Point24, Point.Point22);

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 2);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 2);

        backgammon.pickUpDice();

        backgammon.doubleStake();
        assertEquals(backgammon.getStake(), 4);

        backgammon.reverseDoubleStake();
        assertEquals(backgammon.getStake(), 2);
    }

    private class BackgammonTestState implements BackgammonState {

        private Status status;
        private Color currentPlayerColor;
        private Color cubeOwner;
        private Color winner;
        private Board board = new Board();
        private Die.Face dieOne;
        private Die.Face dieTwo;
        private int dieOneMoves;
        private int dieTwoMoves;
        private int stake;

        public BackgammonTestState() {

        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof BackgammonState) {
                BackgammonState other = (BackgammonState) o;
                if (this.getStatus() != other.getStatus()) {
                    return false;
                } else if (this.getStatus() != other.getStatus()) {
                    return false;
                } else if (this.getCurrentPlayer() != other.getCurrentPlayer()) {
                    return false;
                } else if (this.getCubeOwner() != other.getCubeOwner()) {
                    return false;
                } else if (this.getWinner() != other.getWinner()) {
                    return false;
                } else if (this.getDieOne() != other.getDieOne()) {
                    return false;
                } else if (this.getDieTwo() != other.getDieTwo()) {
                    return false;
                } else if (this.getNumberOfMoves(this.getDieOne()) != other.getNumberOfMoves(other.getDieTwo())) {
                    return false;
                } else if (this.getStake() != other.getStake()) {
                    return false;
                } else {
                    for(Point point : Point.values()) {
                        if(this.getPointState(point) != other.getPointState(point)) {
                            return false;
                        }
                    }
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            // TODO: 15.2.2017. review this
            return board.hashCode();
        }

        @Override
        public Status getStatus() {
            return status;
        }

        @Override
        public Color getCurrentPlayer() {
            return currentPlayerColor;
        }

        @Override
        public Color getCubeOwner() {
            return cubeOwner;
        }

        @Override
        public Color getWinner() {
            return winner;
        }

        @Override
        public PointState getPointState(Point point) {
            return board.getPoint(point);
        }

        @Override
        public Die.Face getDieOne() {
            return dieOne;
        }

        @Override
        public Die.Face getDieTwo() {
            return dieTwo;
        }

        @Override
        public int getNumberOfMoves(Die.Face die) {
            return dieOneMoves;
        }

        @Override
        public int getStake() {
            return dieTwoMoves;
        }

        @Override
        public boolean isMovable() {
            // TODO: 15.2.2017. This is dummy. Is there a better way?
            return false;
        }

        @Override
        public boolean isMovable(Point source) {
            // TODO: 15.2.2017. This is dummy. Is there a better way?
            return false;
        }

        @Override
        public boolean isMovable(Point source, Point destination) {
            // TODO: 15.2.2017. This is dummy. Is there a better way?
            return false;
        }

        @Override
        public boolean isHit(Point source, Point destination) {
            // TODO: 15.2.2017. This is dummy. Is there a better way?
            return false;
        }
    }
    */
}
