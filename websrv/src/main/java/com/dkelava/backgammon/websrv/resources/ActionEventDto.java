package com.dkelava.backgammon.websrv.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

import javax.validation.constraints.NotNull;

public class ActionEventDto {

    @JsonCreator
    public ActionEventDto(
            @JsonProperty("uri") URI uri,
            @JsonProperty("action") String action,
            @JsonProperty("source") String source,
            @JsonProperty("destination") String destination,
            @JsonProperty("isHit") Boolean isHit) {
        this.uri = uri;
        this.action = action;
        this.source = source;
        this.destination = destination;
        this.isHit = isHit;
    }

    public URI getUri() {
        return uri;
    }

    public String getAction() {
        return action;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Boolean getHit() {
        return isHit;
    }

    @NotNull
    private final URI uri;
    @NotNull
    private final String action;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String source;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String destination;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Boolean isHit;
}
