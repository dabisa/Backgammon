package com.dkelava.backgammon.bglib.nn;

import com.dkelava.backgammon.bglib.model.BackgammonState;
import com.dkelava.backgammon.bglib.model.Color;

import org.nd4j.linalg.api.ndarray.INDArray;

public interface OutputCoder {
    int getSize();
    INDArray encode(BackgammonState state, Color color);
}
