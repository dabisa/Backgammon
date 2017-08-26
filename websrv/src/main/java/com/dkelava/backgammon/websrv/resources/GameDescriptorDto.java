package com.dkelava.backgammon.websrv.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Dabisa on 17/08/2017.
 */
public class GameDescriptorDto {

    private final String whitePlayerName;
    private final String blackPlayerName;

    @JsonCreator
    public GameDescriptorDto(
            @JsonProperty("whitePlayerName") String whitePlayerName,
            @JsonProperty("blackPlayerName") String blackPlayerName) {
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }
}
