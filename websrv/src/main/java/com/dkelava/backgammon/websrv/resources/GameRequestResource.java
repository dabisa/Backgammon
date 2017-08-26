package com.dkelava.backgammon.websrv.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class GameRequestResource extends ResourceSupport {

    public GameRequestResource(String challenger, String opponent, boolean accepted) {
        this.challenger = challenger;
        this.opponent = opponent;
        this.accepted = accepted;
    }

    public String getChallenger() {
        return challenger;
    }

    public String getOpponent() {
        return opponent;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @JsonProperty
    private final String challenger;
    @JsonProperty
    private final String opponent;
    @JsonProperty
    private final boolean accepted;
}
