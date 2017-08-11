package com.dkelava.backgammon.bglib.game.actions.actions;

import com.dkelava.backgammon.bglib.game.Game;
import com.dkelava.backgammon.bglib.model.Backgammon;

public class AcceptDoubleAction extends Action {

    private static String id = "A";

    static {
        addDecoder(id, new Decoder() {
            @Override
            public Action decode(String value) throws Exception {
                return AcceptDoubleAction.decode(value);
            }
        });
    }

    public static Action decode(String value) throws Exception {
        if(value.substring(0,1).equals(id)) {
            return new AcceptDoubleAction();
        } else {
            throw new IllegalArgumentException("invalid accept action encoding");
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
        backgammon.acceptDouble();
        if(observer!= null) {
            observer.onDoubleAccepted(backgammon.getState());
        }
    }

    @Override
    public void undo(Backgammon backgammon, Game.Observer observer) {
        backgammon.undoAcceptDouble();
        if(observer!= null) {
            observer.onUndoDoubleAccepted(backgammon.getState());
        }
    }
}
