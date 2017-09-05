package com.dkelava.backgammon.bglib.game;

import com.dkelava.backgammon.bglib.model.BackgammonState;
import com.dkelava.backgammon.bglib.model.Die;
import com.dkelava.backgammon.bglib.model.Point;

public class GameObserverBase implements com.dkelava.backgammon.bglib.game.Game.Observer {
    @Override
    public void onInitialized(BackgammonState state) {}

    @Override
    public void onUpdate(BackgammonState state) {}

    @Override
    public void onInitialRoll(BackgammonState state) {}

    @Override
    public void onUndoInitialRoll(BackgammonState state) {}

    @Override
    public void onRoll(BackgammonState state) {}

    @Override
    public void onUndoRoll(BackgammonState state) {}

    @Override
    public void onDiceCleared(BackgammonState state) {}

    @Override
    public void onUndoDiceCleared(BackgammonState state, Die.Face prevDieOne, Die.Face prevDieTwo) {}

    @Override
    public void onMove(BackgammonState state, Point source, Point destination, boolean isHit) {}

    @Override
    public void onUndoMove(BackgammonState state, Point source, Point destination, boolean isHit) {}

    @Override
    public void onDouble(BackgammonState state) {}

    @Override
    public void onUndoDouble(BackgammonState state) {}

    @Override
    public void onDoubleAccepted(BackgammonState state) {}

    @Override
    public void onUndoDoubleAccepted(BackgammonState state) {}

    @Override
    public void onSurrender(BackgammonState state) {}

    @Override
    public void onUndoSurrender(BackgammonState state) {}
}
