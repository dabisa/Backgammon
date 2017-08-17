package com.dkelava.backgammon.websrv.web;

/**
 * Created by Dabisa on 16/08/2017.
 */
public enum GameAction {
    Roll("roll"),
    Move("move"),
    PickUpDice("pickUpDice"),
    OfferDouble("offerDouble"),
    AcceptDouble("acceptDouble"),
    Quit("quit"),
    Invalid("invalid");

    GameAction(String name) {
        this.name = name;
    }

    public static GameAction parse(String name) {
        if(name.equals(Roll.name)) {
            return Roll;
        } else if(name.equals(Move.name)) {
            return Move;
        } else if(name.equals(PickUpDice.name)) {
            return PickUpDice;
        } else if(name.equals(OfferDouble.name)) {
            return OfferDouble;
        } else if(name.equals(AcceptDouble.name)) {
            return AcceptDouble;
        } else if(name.equals(Quit.name)) {
            return Quit;
        } else {
            return Invalid;
        }
    }

    private String name;
}
