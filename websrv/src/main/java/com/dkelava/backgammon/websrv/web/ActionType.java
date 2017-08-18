package com.dkelava.backgammon.websrv.web;

import java.util.HashMap;
import java.util.Map;

public enum ActionType {
    Roll("roll"),
    Move("move"),
    PickUpDice("pickUpDice"),
    OfferDouble("offerDouble"),
    AcceptDouble("acceptDouble"),
    Quit("quit");

    ActionType(String name) {
        this.name = name;
    }

    private static final Map<String, ActionType> myMap = new HashMap<>();
    static
    {
        for(ActionType actionType : ActionType.values()) {
            myMap.put(actionType.name, actionType);
        }
    }

    public static ActionType parse(String name) {
        return myMap.get(name);
    }

    private final String name;
}
