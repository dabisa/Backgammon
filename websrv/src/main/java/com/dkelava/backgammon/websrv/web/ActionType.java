package com.dkelava.backgammon.websrv.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ActionType {
    @JsonProperty("roll")
    Roll,
    @JsonProperty("move")
    Move,
    @JsonProperty("pickUpDice")
    PickUpDice,
    @JsonProperty("offerDouble")
    OfferDouble,
    @JsonProperty("acceptDouble")
    AcceptDouble,
    @JsonProperty("quit")
    Quit
}
