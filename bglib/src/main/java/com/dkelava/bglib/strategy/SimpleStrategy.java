package com.dkelava.bglib.strategy;

import com.dkelava.bglib.game.actions.actions.AcceptDoubleAction;
import com.dkelava.bglib.game.actions.actions.Action;
import com.dkelava.bglib.game.actions.actions.ClearDiceAction;
import com.dkelava.bglib.game.actions.actions.MoveAction;
import com.dkelava.bglib.game.actions.actions.InitialRollAction;
import com.dkelava.bglib.game.actions.actions.RollAction;
import com.dkelava.bglib.model.BackgammonState;
import com.dkelava.bglib.model.Point;

public class SimpleStrategy implements Strategy {
    @Override
    public Action play(BackgammonState state) {

        switch (state.getStatus()) {

            case Initial:
                return new InitialRollAction();

            case NoMoves:
                return new ClearDiceAction();

            case Moving:
                for (Point source : Point.getPoints(state.getCurrentPlayer())) {
                    if (state.getMoves().isMovable(source)) {
                        for (Point destination : Point.getPoints(state.getCurrentPlayer())) {
                            if (state.getMoves().isMovable(source, destination)) {
                                return new MoveAction(source, destination);
                            }
                        }
                    }
                }
                break;

            case Rolling:
                return new RollAction();

            case DoubleStake:
                return new AcceptDoubleAction();
        }

        return null;
    }
}
