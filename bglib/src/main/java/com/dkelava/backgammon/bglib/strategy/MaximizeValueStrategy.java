package com.dkelava.backgammon.bglib.strategy;

import com.dkelava.backgammon.bglib.game.actions.actions.AcceptDoubleAction;
import com.dkelava.backgammon.bglib.game.actions.actions.Action;
import com.dkelava.backgammon.bglib.game.actions.actions.ClearDiceAction;
import com.dkelava.backgammon.bglib.game.actions.actions.InitialRollAction;
import com.dkelava.backgammon.bglib.game.actions.actions.MoveAction;
import com.dkelava.backgammon.bglib.game.actions.actions.RollAction;
import com.dkelava.backgammon.bglib.model.Backgammon;
import com.dkelava.backgammon.bglib.model.BackgammonState;
import com.dkelava.backgammon.bglib.model.Move;
import com.dkelava.backgammon.bglib.model.MoveNode;

import java.util.List;

public abstract class MaximizeValueStrategy implements Strategy {

    @Override
    public Action play(BackgammonState state) {
        switch (state.getStatus()) {

            case Initial:
                return new InitialRollAction();

            case NoMoves:
                return new ClearDiceAction();

            case Moving:
                NodeSelector nodeSelector = new NodeSelector(state);
                Move move = nodeSelector.selectMove();
                return new MoveAction(move.getSource(), move.getDestination());

            case Rolling:
                return new RollAction();

            case DoubleStake:
                return new AcceptDoubleAction();
        }

        return null;
    }

    public abstract double evaluate(BackgammonState state);


    private class NodeSelector implements MoveNode.Visitor  {

        final Backgammon backgammon;
        private double maxValue = 0;
        private MoveNode selectedNode = null;

        NodeSelector(BackgammonState state) {
            backgammon = new Backgammon(state);
        }

        @Override
        public void enter(Move move) {
            //System.err.print(backgammon.encode() + " do" + move.encode() + "\n");

            backgammon.move(move.getSource(), move.getDestination());

            //System.err.print(backgammon.encode() + "\n");

            if(backgammon.getState().getMoves().isEnd()) {
                double value = evaluate(backgammon.getState());
                if(value > maxValue || selectedNode == null) {
                    maxValue = value;
                    selectedNode = backgammon.getState().getMoves();
                }
            }
        }

        @Override
        public void exit(Move move) {
            //System.err.print(backgammon.encode() + " undo" + move.encode() + "\n");

            backgammon.undoMove(move.getSource(), move.getDestination());

            //System.err.print(backgammon.encode() + "\n");
        }

        public Move selectMove() {
            MoveNode root = backgammon.getState().getMoves();
            root.visit(this);
            List<Move> moves = selectedNode.getMoves(root);
            return moves.get(0);
        }
    }
}
