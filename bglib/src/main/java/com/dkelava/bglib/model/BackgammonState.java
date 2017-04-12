package com.dkelava.bglib.model;

/**
 * This interface represents immutable backgammon state
 */
public interface BackgammonState {
    Status getStatus();

    Color getCurrentPlayer();

    Color getCubeOwner();

    Color getWinner();

    PointState getPointState(Point point);

    Die.Face getDieOne();

    Die.Face getDieTwo();

    MoveNode getMoves();

    int getStake();

    boolean isGammon();
}
