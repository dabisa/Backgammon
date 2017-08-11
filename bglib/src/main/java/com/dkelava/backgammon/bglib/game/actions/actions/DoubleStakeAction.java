package com.dkelava.backgammon.bglib.game.actions.actions;

import com.dkelava.backgammon.bglib.game.Game;
import com.dkelava.backgammon.bglib.model.Backgammon;

public class DoubleStakeAction extends Action {

    private static String id = "D";

    static {
        addDecoder(id, new Decoder() {
            @Override
            public Action decode(String value) throws Exception {
                return DoubleStakeAction.decode(value);
            }
        });
    }

    public static Action decode(String value) throws Exception {
        if(value.substring(0,1).equals(id)) {
            return new DoubleStakeAction();
        } else {
            throw new IllegalArgumentException("invalid double action encoding");
        }
    }

    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        return builder.toString();
    }

    @Override
    public void execute(Backgammon backgammon, Game.Observer observer) {
        backgammon.doubleStake();
        if(observer!= null) {
            observer.onDouble(backgammon.getState());
        }
    }

    @Override
    public void undo(Backgammon backgammon, Game.Observer observer) {
        backgammon.undoDoubleStake();
        if(observer!= null) {
            observer.onUndoDouble(backgammon.getState());
        }
    }
}
