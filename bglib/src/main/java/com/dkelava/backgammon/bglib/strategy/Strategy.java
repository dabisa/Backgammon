package com.dkelava.backgammon.bglib.strategy;

import com.dkelava.backgammon.bglib.game.actions.actions.Action;
import com.dkelava.backgammon.bglib.model.BackgammonState;

public interface Strategy {
    Action play(BackgammonState state);
}
