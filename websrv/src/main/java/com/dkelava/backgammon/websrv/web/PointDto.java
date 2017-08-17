package com.dkelava.backgammon.websrv.web;

import com.dkelava.backgammon.bglib.model.PointState;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for point state
 */
public class PointDto {

    @JsonProperty
    private final String color;
    @JsonProperty
    private final Integer count;

    public PointDto(PointState pointState) {
        this.color = pointState.getOwner().encode();
        this.count = pointState.getQuantity();
    }
}
