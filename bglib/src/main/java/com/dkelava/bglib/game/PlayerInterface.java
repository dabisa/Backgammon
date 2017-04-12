package com.dkelava.bglib.game;

import com.dkelava.bglib.game.actions.actions.Action;
import com.dkelava.bglib.model.BackgammonState;

public interface PlayerInterface {
    BackgammonState getState();
    void execute(Action action) throws Exception;
}
