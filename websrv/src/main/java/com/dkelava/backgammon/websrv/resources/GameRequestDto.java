package com.dkelava.backgammon.websrv.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class GameRequestDto {

    @JsonCreator
    public GameRequestDto(
            @JsonProperty("opponent") String opponent) {
        this.opponent = opponent;
    }

    public String getOpponent() {
        return opponent;
    }

    @NotNull
    private String opponent;
}
