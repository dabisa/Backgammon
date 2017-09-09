package com.dkelava.backgammon.websrv.resources;

import com.dkelava.backgammon.websrv.exceptions.InvalidActionException;

import java.util.HashMap;
import java.util.Map;

public enum ActionType {
    Roll("roll"),
    Move("move"),
    PickUpDice("pickUpDice"),
    OfferDouble("offerDouble"),
    RejectDouble("rejectDouble"),
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
        ActionType action = myMap.get(name);
        if(action != null) {
            return action;
        } else {
            throw new InvalidActionException("Unknown action: " + name);
        }
    }

    public String getName() {
        return name;
    }

    private final String name;
}
