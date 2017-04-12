package com.dkelava.bglib.nn;

import com.dkelava.bglib.model.BackgammonState;
import com.dkelava.bglib.model.Color;

import org.nd4j.linalg.api.ndarray.INDArray;

public interface InputCoder {
    int getInputSize();
    INDArray encode(BackgammonState state, Color color);
}
