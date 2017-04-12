package com.dkelava.bglib.game.actions.actions;

import com.dkelava.bglib.game.Game;
import com.dkelava.bglib.model.Backgammon;

public class RejectDoubleAction extends Action {

    private static String id = "N";

    static {
        addDecoder(id, new Decoder() {
            @Override
            public Action decode(String value) throws Exception {
                return RejectDoubleAction.decode(value);
            }
        });
    }

    public static Action decode(String value) throws Exception {
        if(value.substring(0,1).equals(id)) {
            return new RejectDoubleAction();
        } else {
            throw new IllegalArgumentException("invalid reject action encoding");
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
        backgammon.rejectDouble();
        if(observer!= null) {
            observer.onSurrender(backgammon.getState());
        }
    }

    @Override
    public void undo(Backgammon backgammon, Game.Observer observer) {
        backgammon.undoRejectDouble();
        if(observer!= null) {
            observer.onUndoSurrender(backgammon.getState());
        }
    }
}
