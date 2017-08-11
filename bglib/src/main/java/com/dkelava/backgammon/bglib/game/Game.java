package com.dkelava.backgammon.bglib.game;

import com.dkelava.backgammon.bglib.game.actions.actions.Action;
import com.dkelava.backgammon.bglib.model.Backgammon;
import com.dkelava.backgammon.bglib.model.BackgammonState;
import com.dkelava.backgammon.bglib.model.Color;
import com.dkelava.backgammon.bglib.model.Die;
import com.dkelava.backgammon.bglib.model.Point;
import com.dkelava.backgammon.bglib.model.Status;

public class Game {

    private final History history = new History();
    private Backgammon backgammon;
    private Player whitePlayer;
    private Player blackPlayer;
    private boolean enabled = false;
    private Observer observer;

    private MyPlayerInterface whitePlayerInterface = new MyPlayerInterface(Color.White);
    private MyPlayerInterface blackPlayerInterface = new MyPlayerInterface(Color.Black);

    public BackgammonState getState() {
        return backgammon.getState();
    }

    public Game(Observer observer) {
        backgammon = new Backgammon();
        this.observer = observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public void removeObserver() {
        this.observer = null;
    }

    public void initialize() {
        backgammon.initialize();
        history.clear();
    }
/*
    public void loadGame(Backgammon.Builder builder, String historyString) throws Exception {
        // TODO: 18.2.2017. ne stvarati novi objekt
        this.backgammon = builder.build();
        observer.onUpdate(this.backgammon.getState());

        // TODO: 22.2.2017. restore history
        history.restore(historyString);
    }
  */

    public void loadGame(String state) throws Exception {
        String[] data = state.split("#");
        if(data.length < 1 || data.length > 2) {
            throw new IllegalArgumentException("invalid game encoding: " + state);
        }

        this.backgammon.restore(data[0]);
        if(data.length > 1) {
            this.history.restore(data[1]);
        } else {
            history.clear();
        }
    }

    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(backgammon.encode());
        builder.append("#");
        builder.append(history.encode());
        return builder.toString();
    }

    public void setPlayer(Color color, Player player) {
        switch (color) {
            case White:
                whitePlayer = player;
                break;
            case Black:
                blackPlayer = player;
                break;
        }
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void next() {
        if (enabled) {
            if (whitePlayer != null && blackPlayer != null) {
                Status status = backgammon.getState().getStatus();
                if (status != Status.Empty && status != Status.End) {
                    switch (backgammon.getState().getCurrentPlayer()) {
                        case White:
                            blackPlayer.onDeactivate();
                            whitePlayer.onActivate(whitePlayerInterface);
                            break;
                        case Black:
                            whitePlayer.onDeactivate();
                            blackPlayer.onActivate(blackPlayerInterface);
                            break;
                    }
                }
            } else {
                throw new RuntimeException("Players not initialized");
            }
        }
    }

    public boolean canUndo() {
        return history.hasPrevious();
    }

    public boolean canRedo() {
        return history.hasNext();
    }

    public void undo() {
        if (canUndo()) {
            Action action = history.getPrevious();
            action.undo(backgammon, observer);
        }
    }

    public void redo() throws Exception {
        if (canRedo()) {
            Action action = history.getNext();
            action.execute(backgammon, observer);
        }
    }

    private class MyPlayerInterface implements PlayerInterface {

        private Color color;

        MyPlayerInterface(Color color) {
            this.color = color;
        }

        @Override
        public BackgammonState getState() {
            return backgammon.getState();
        }

        @Override
        public void execute(Action action) throws Exception {
            if (color == backgammon.getState().getCurrentPlayer()) {
                history.add(action);
                action.execute(backgammon, observer);
            } else {
                // wrong player
            }
        }
    }

    public interface Observer {
        void onInitialized(BackgammonState state);

        void onUpdate(BackgammonState state);

        void onInitialRoll(BackgammonState state);

        void onUndoInitialRoll(BackgammonState state);

        void onRoll(BackgammonState state);

        void onUndoRoll(BackgammonState state);

        void onDiceCleared(BackgammonState state);

        void onUndoDiceCleared(BackgammonState state, Die.Face prevDieOne, Die.Face prevDieTwo);

        void onMove(BackgammonState state, Point source, Point destination, boolean isHit);

        void onUndoMove(BackgammonState state, Point source, Point destination, boolean isHit);

        void onDouble(BackgammonState state);

        void onUndoDouble(BackgammonState state);

        void onDoubleAccepted(BackgammonState state);

        void onUndoDoubleAccepted(BackgammonState state);

        void onSurrender(BackgammonState state);

        void onUndoSurrender(BackgammonState state);
    }
}
