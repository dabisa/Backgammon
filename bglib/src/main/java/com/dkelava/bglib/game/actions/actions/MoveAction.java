package com.dkelava.bglib.game.actions.actions;

import com.dkelava.bglib.game.Game;
import com.dkelava.bglib.model.Backgammon;
import com.dkelava.bglib.model.MoveNode;
import com.dkelava.bglib.model.Point;

public class MoveAction extends Action {

    private final Point source;
    private final Point destination;

    public MoveAction(Point source, Point destination) {
        this.source = source;
        this.destination = destination;
    }

    static String id = "M";

    static {
        addDecoder(id, new Decoder() {
            @Override
            public Action decode(String value) throws Exception {
                return MoveAction.decode(value);
            }
        });
    }

    public static Action decode(String value) throws Exception {
        if(value.substring(0,1).equals(id)) {
            Point source = Point.decode(value.substring(1,3));
            Point destination = Point.decode(value.substring(3,5));
            return new MoveAction(source, destination);
        } else {
            throw new IllegalArgumentException("invalid reject action encoding");
        }
    }

    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(source.encode());
        builder.append(destination.encode());
        return builder.toString();
    }

    @Override
    public void execute(Backgammon backgammon, Game.Observer observer) throws Exception{
        MoveNode moveNode = backgammon.getState().getMoves();
        if(moveNode != null) {
            boolean isHit = moveNode.isHit(source, destination);
            backgammon.move(source, destination);
            if(observer != null) {
                observer.onMove(backgammon.getState(), source, destination, isHit);
            }
        } else {
            throw new IllegalStateException("execute move failed for " + backgammon.encode());
        }
    }

    @Override
    public void undo(Backgammon backgammon, Game.Observer observer) {
        boolean isHit = backgammon.getState().getMoves().getMove().isHit();
        backgammon.undoMove(source, destination);
        if(observer != null) {
            observer.onUndoMove(backgammon.getState(), source, destination, isHit);
        }
    }

}
