package com.dkelava.backgammon.bglib.game;

import com.dkelava.backgammon.bglib.game.actions.actions.Action;
import com.dkelava.backgammon.bglib.model.BackgammonState;

public interface PlayerInterface {
    BackgammonState getState();
    void execute(Action action) throws Exception;
}
