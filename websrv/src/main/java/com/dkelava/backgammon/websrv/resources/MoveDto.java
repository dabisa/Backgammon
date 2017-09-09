package com.dkelava.backgammon.websrv.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Created by Dabisa on 08/09/2017.
 */
public class MoveDto {

    @JsonCreator
    public MoveDto(
            @JsonProperty("source") String source,
            @JsonProperty("destination") String destination,
            @JsonProperty("dice") List<Integer> dice) {
        this.source = source;
        this.destination = destination;
        this.dice = dice;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Collection<Integer> getDice() {
        return dice;
    }

    public Collection<MoveDto> getMoves() {
        return moves;
    }

    @NotNull
    private final String source;
    @NotNull
    private final String destination;
    @NotNull
    private final Collection<Integer> dice;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Collection<MoveDto> moves = new LinkedList<>();
}
