package com.dkelava.bglib.strategy;

import com.dkelava.bglib.game.actions.actions.Action;
import com.dkelava.bglib.model.BackgammonState;

public interface Strategy {
    Action play(BackgammonState state);
}
