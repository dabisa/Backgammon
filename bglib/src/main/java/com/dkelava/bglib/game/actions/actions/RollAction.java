package com.dkelava.bglib.game.actions.actions;

import com.dkelava.bglib.game.Game;
import com.dkelava.bglib.model.Die;
import com.dkelava.bglib.model.Backgammon;

public class RollAction extends Action {

    private Die.Face dieOne = null;
    private Die.Face dieTwo = null;

    public RollAction() {}

    private RollAction(Die.Face dieOne, Die.Face dieTwo) {
        this.dieOne = dieOne;
        this.dieTwo = dieTwo;
    }

    static String id = "R";

    static {
        addDecoder(id, new Decoder() {
            @Override
            public Action decode(String value) throws Exception {
                return RollAction.decode(value);
            }
        });
    }

    public static Action decode(String value) throws Exception {
        if(value.substring(0,1).equals(id)) {
            Die.Face dieOne = Die.Face.decode(value.substring(1, 2));
            Die.Face dieTwo = Die.Face.decode(value.substring(2, 3));
            return new RollAction(dieOne, dieTwo);

        } else {
            throw new IllegalArgumentException("invalid reject action encoding");
        }

    }

    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(dieOne.encode());
        builder.append(dieTwo.encode());
        return builder.toString();
    }

    @Override
    public void execute(Backgammon backgammon, Game.Observer observer) {
        backgammon.roll(dieOne, dieTwo);
        dieOne = backgammon.getState().getDieOne();
        dieTwo = backgammon.getState().getDieTwo();
        if(observer!= null) {
            observer.onRoll(backgammon.getState());
        }
    }

    @Override
    public void undo(Backgammon backgammon, Game.Observer observer) {
        backgammon.undoRoll();
        if(observer!= null) {
            observer.onUndoRoll(backgammon.getState());
        }
    }
}
