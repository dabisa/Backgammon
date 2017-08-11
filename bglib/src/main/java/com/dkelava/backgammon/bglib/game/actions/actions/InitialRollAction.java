package com.dkelava.backgammon.bglib.game.actions.actions;

import com.dkelava.backgammon.bglib.game.Game;
import com.dkelava.backgammon.bglib.model.Die;
import com.dkelava.backgammon.bglib.model.Backgammon;

public class InitialRollAction extends Action {

    private Die.Face dieOne;
    private Die.Face dieTwo;
    private Die.Face prevDie;

    public InitialRollAction() {}

    private InitialRollAction(Die.Face dieOne, Die.Face dieTwo, Die.Face prevDie) {
        this.dieOne = dieOne;
        this.dieTwo = dieTwo;
        this.prevDie = prevDie;
    }

    static String id = "I";

    static {
        addDecoder(id, new Decoder() {
            @Override
            public Action decode(String value) throws Exception {
                return InitialRollAction.decode(value);
            }
        });
    }

    public static Action decode(String value) throws Exception {
        if (value.substring(0, 1).equals(id)) {
            Die.Face dieOne = Die.Face.decode(value.substring(1, 2));
            Die.Face dieTwo = Die.Face.decode(value.substring(2, 3));
            Die.Face prevDie = Die.Face.decode(value.substring(3, 4));
            return new InitialRollAction(dieOne, dieTwo, prevDie);
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
        builder.append(prevDie.encode());
        return builder.toString();
    }

    @Override
    public void execute(Backgammon backgammon, Game.Observer observer) {
        Die.Face prevDie = backgammon.getState().getDieOne();
        backgammon.roll(dieOne, dieTwo);
        this.prevDie = prevDie;
        dieOne = backgammon.getState().getDieOne();
        dieTwo = backgammon.getState().getDieTwo();
        if(observer != null) {
            observer.onInitialRoll(backgammon.getState());
        }
    }

    @Override
    public void undo(Backgammon backgammon, Game.Observer observer) {
        if(prevDie != null) {
            backgammon.undoInitialRoll(prevDie);
            if(observer != null) {
                observer.onUndoInitialRoll(backgammon.getState());
            }
        }
    }
}
