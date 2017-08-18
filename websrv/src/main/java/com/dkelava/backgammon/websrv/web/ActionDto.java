package com.dkelava.backgammon.websrv.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class ActionDto {

    @NotNull
    private final ActionType action;
    private final String source;
    private final String destination;

    @JsonCreator
    public ActionDto(
            @JsonProperty("action") ActionType action,
            @JsonProperty("source") String source,
            @JsonProperty("destination") String destination) {
        this.action = action;
        this.source = source;
        this.destination = destination;
    }

    public final ActionType getAction() {
        return action;
    }

    public final String getSource() {
        return source;
    }

    public final String getDestination() {
        return destination;
    }
}
