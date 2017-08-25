package com.dkelava.backgammon.websrv.resources;

import com.dkelava.backgammon.bglib.model.BackgammonState;
import com.dkelava.backgammon.bglib.model.Color;
import com.dkelava.backgammon.bglib.model.Point;
import com.dkelava.backgammon.bglib.model.PointState;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dabisa on 12/08/2017.
 */
public class GameResource extends ResourceSupport {

    @JsonProperty
    private final String status;
    @JsonProperty
    private final String currentPlayer;
    @JsonProperty
    private final String winner;
    @JsonProperty
    private final Integer dieOne;
    @JsonProperty
    private final Integer dieTwo;
    @JsonProperty
    private final String whiteHome;
    @JsonProperty
    private final String whiteBar;
    @JsonProperty
    private final String blackHome;
    @JsonProperty
    private final String blackBar;
    @JsonProperty
    private final List<String> points = new ArrayList<>(24);

    public GameResource(BackgammonState backgammonState) {
        this.status = backgammonState.getStatus().toString().toLowerCase();
        this.currentPlayer = backgammonState.getCurrentPlayer().toString().toLowerCase();
        this.winner = backgammonState.getWinner().toString().toLowerCase();
        this.dieOne = backgammonState.getDieOne().getValue();
        this.dieTwo = backgammonState.getDieTwo().getValue();
        this.whiteHome = encodePointState(backgammonState.getPointState(Point.WhiteHome));
        this.whiteBar = encodePointState(backgammonState.getPointState(Point.WhiteBar));
        this.blackHome = encodePointState(backgammonState.getPointState(Point.BlackHome));
        this.blackBar = encodePointState(backgammonState.getPointState(Point.BlackBar));
        for(int i = 1; i <= 24; ++i) {
            points.add(encodePointState(backgammonState.getPointState(Point.getPoint(Color.White, i))));
        }
    }

    private String encodePointState(PointState pointState) {
        if(pointState == PointState.Empty) {
            return "N";
        } else if(pointState.isOwner(Color.White)) {
            return "W" + pointState.getQuantity();
        } else {
            return "B" + pointState.getQuantity();
        }
    }
}
