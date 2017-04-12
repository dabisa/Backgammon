package com.dkelava.bglib.nn;

import com.dkelava.bglib.model.BackgammonState;
import com.dkelava.bglib.model.Color;
import com.dkelava.bglib.model.Point;
import com.dkelava.bglib.model.PointState;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class SimpleInputCoder implements InputCoder {
    
    @Override
    public int getInputSize() {
        return 52;
    }

    @Override
    public INDArray encode(BackgammonState state, Color myColor) {
        return Nd4j.create(new double[][] {{
            encodePointState(state.getPointState(Point.getBar(myColor)), myColor),
            encodePointState(state.getPointState(Point.getBar(myColor.getOpponent())), myColor),
            encodePointState(state.getPointState(Point.getHome(myColor)), myColor),
            encodePointState(state.getPointState(Point.getHome(myColor.getOpponent())), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 1)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 2)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 3)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 4)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 5)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 6)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 7)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 8)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 9)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 10)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 11)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 12)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 13)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 14)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 15)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 16)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 17)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 18)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 19)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 20)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 21)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 22)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 23)), myColor),
            encodePointState(state.getPointState(Point.getPoint(myColor, 24)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 1)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 2)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 3)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 4)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 5)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 6)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 7)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 8)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 9)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 10)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 11)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 12)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 13)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 14)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 15)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 16)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 17)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 18)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 19)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 20)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 21)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 22)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 23)), myColor),
            encodeBlotState(state.getPointState(Point.getPoint(myColor, 24)), myColor),
        }});
    }

    double encodePointState(PointState pointState, Color myColor) {
        Color owner = pointState.getOwner();
        int quantity = pointState.getQuantity();
        if(myColor == owner) {
            return quantity / 15.0;
        } else {
            return -quantity / 15.0;
        }
    }

    double encodeBlotState(PointState pointState, Color myColor) {
        Color owner = pointState.getOwner();
        int quantity = pointState.getQuantity();
        if(myColor == owner) {
            return quantity == 1 ? -1.0 : 0;
        } else {
            return quantity == 1 ? 1.0 : 0;
        }
    }
}
