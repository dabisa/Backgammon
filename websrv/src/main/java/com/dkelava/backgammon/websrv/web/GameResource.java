package com.dkelava.backgammon.websrv.web;

import com.dkelava.backgammon.bglib.model.BackgammonState;
import com.dkelava.backgammon.bglib.model.Color;
import com.dkelava.backgammon.bglib.model.Point;
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
    private final PointDto whiteHome;
    @JsonProperty
    private final PointDto whiteBar;
    @JsonProperty
    private final PointDto blackHome;
    @JsonProperty
    private final PointDto blackBar;
    @JsonProperty
    private final List<PointDto> points = new ArrayList<>(24);

    public GameResource(BackgammonState backgammon) {
        this.status = backgammon.getStatus().toString();
        this.currentPlayer = backgammon.getCurrentPlayer().toString();
        this.winner = backgammon.getWinner().toString();
        this.dieOne = backgammon.getDieOne().getValue();
        this.dieTwo = backgammon.getDieTwo().getValue();
        this.whiteHome = new PointDto(backgammon.getPointState(Point.WhiteHome));
        this.whiteBar = new PointDto(backgammon.getPointState(Point.WhiteBar));
        this.blackHome = new PointDto(backgammon.getPointState(Point.BlackHome));
        this.blackBar = new PointDto(backgammon.getPointState(Point.BlackBar));
        for(int i = 1; i <= 24; ++i) {
            points.add(new PointDto(backgammon.getPointState(Point.getPoint(Color.White, i))));
        }
    }

    //@JsonCreator
    //public GameResource(@JsonProperty String state)
}
