package com.dkelava.bglib.nn;

import com.dkelava.bglib.model.BackgammonState;
import com.dkelava.bglib.model.Color;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class SimpleOutputCoder implements OutputCoder {

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public INDArray encode(BackgammonState backgammonState, Color color) {
        boolean playerWins = backgammonState.getWinner() == color;
        boolean isGammon = backgammonState.isGammon();
        return Nd4j.create(new double[][] {{
                playerWins ? 1.0 : -1.0,
                !isGammon ? 0.0 : (playerWins ? 1.0 : -1.0)
        }});
    }
}
