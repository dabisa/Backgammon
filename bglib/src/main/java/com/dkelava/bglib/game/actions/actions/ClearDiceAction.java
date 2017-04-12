package com.dkelava.bglib.game.actions.actions;

import com.dkelava.bglib.game.Game;
import com.dkelava.bglib.model.Die;
import com.dkelava.bglib.model.Backgammon;
import com.dkelava.bglib.model.Move;
import com.dkelava.bglib.model.MoveNode;
import com.dkelava.bglib.model.Status;

import java.util.LinkedList;
import java.util.List;

public class ClearDiceAction extends Action {

    private Die.Face dieOne;
    private Die.Face dieTwo;
    private List<Move> moves;

    public ClearDiceAction() {
        moves = new LinkedList<>();
    }

    private ClearDiceAction(Die.Face dieOne, Die.Face dieTwo, List<Move> moves) {
        this.dieOne = dieOne;
        this.dieTwo = dieTwo;
        this.moves = moves;
    }

    private static String id = "C";

    static {
        addDecoder(id, new Decoder() {
            @Override
            public Action decode(String value) throws Exception {
                return ClearDiceAction.decode(value);
            }
        });
    }

    public static Action decode(String value) throws Exception {
        if(value.substring(0,1).equals(id)) {
            if(value.length() < 3) {
                throw new IllegalArgumentException("invalid action code");
            }

            Die.Face dieOne = Die.Face.decode(value.substring(1, 2));
            Die.Face dieTwo = Die.Face.decode(value.substring(2, 3));

            List<Move> moves = new LinkedList<>();
            String[] moveTokens = value.split(":");
            for(int i = 1; i < moveTokens.length; ++i) {
                String moveToken = moveTokens[i];
                Move move = Move.decode(moveToken);
                moves.add(move);
            }

            return new ClearDiceAction(dieOne, dieTwo, moves);
        } else {
            throw new IllegalArgumentException("invalid double action encoding");
        }
    }

    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(dieOne.encode());
        builder.append(dieTwo.encode());
        for(Move move : moves) {
            builder.append(":");
            builder.append(move.encode());
        }
        return builder.toString();
    }

    @Override
    public void execute(Backgammon backgammon, Game.Observer observer) {
        dieOne = backgammon.getState().getDieOne();
        dieTwo = backgammon.getState().getDieTwo();
        moves.clear();
        if(backgammon.getState().getStatus() == Status.NoMoves) {
            MoveNode last = backgammon.getState().getMoves();
            MoveNode first = last.getRoot();
            if(first != last) {
                moves.addAll(last.getMoves(first));
            }
        }
        backgammon.clearDice();
        if(observer!= null) {
            observer.onDiceCleared(backgammon.getState());
        }
    }

    @Override
    public void undo(Backgammon backgammon, Game.Observer observer) {
        backgammon.undoClearDice(dieOne, dieTwo, moves);
        if(observer!= null) {
            observer.onUndoDiceCleared(backgammon.getState(), dieOne, dieTwo);
        }
    }
}
