package com.dkelava.backgammon.websrv.resources;

import com.dkelava.backgammon.bglib.model.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.math3.util.Pair;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by Dabisa on 12/08/2017.
 */
public class GameResource extends ResourceSupport {

    public static class DieDto {
        public DieDto(int die, int moves) {
            this.die = die;
            this.moves = moves;
        }
        public int die;
        public int moves;
    };

    public static class MoveDto {

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

    @JsonProperty
    private final String status;
    @JsonProperty
    private final String currentPlayer;
    @JsonProperty
    private final String cubeOwner;
    @JsonProperty
    private final String winner;
    @JsonProperty
    private final List<DieDto> dice = new LinkedList<>();
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
    @JsonProperty
    private final List<MoveDto> availableMoves = new ArrayList<>();

    public GameResource(BackgammonState backgammonState) {
        this.status = backgammonState.getStatus().toString();
        this.currentPlayer = backgammonState.getCurrentPlayer().toString();
        this.cubeOwner = backgammonState.getCubeOwner().toString();
        this.winner = backgammonState.getWinner().toString();
        this.whiteHome = encodePointState(backgammonState.getPointState(Point.WhiteHome));
        this.whiteBar = encodePointState(backgammonState.getPointState(Point.WhiteBar));
        this.blackHome = encodePointState(backgammonState.getPointState(Point.BlackHome));
        this.blackBar = encodePointState(backgammonState.getPointState(Point.BlackBar));
        for(int i = 1; i <= 24; ++i) {
            points.add(encodePointState(backgammonState.getPointState(Point.getPoint(Color.White, i))));
        }
        Die.Face dieOne = backgammonState.getDieOne();
        Die.Face dieTwo = backgammonState.getDieTwo();
        MoveNode moveNode = backgammonState.getMoves();
        if(moveNode != null) {
            generateMoves(moveNode, availableMoves);
            DiceSet diceState = moveNode.getDiceState();
            this.dice.add(new DieDto(dieOne.getValue(), diceState.getNumberOfMoves(dieOne)));
            if(dieOne != dieTwo) {
                this.dice.add(new DieDto(dieTwo.getValue(), diceState.getNumberOfMoves(dieTwo)));
            }
        } else if(dieOne != Die.Face.None && dieTwo != Die.Face.None){
            this.dice.add(new DieDto(dieOne.getValue(), 0));
            this.dice.add(new DieDto(dieTwo.getValue(), 0));
        }
    }

    private static void generateMoves(MoveNode moveNode, Collection<MoveDto> availableMoves) {
        for(Pair<Point, Point> move : moveNode.getAvailableMoves()) {
            PointId source = PointId.from(move.getKey());
            PointId destination = PointId.from(move.getValue());
            List<Integer> dice = new LinkedList<>();
            MoveNode nextNode = moveNode.find(move.getKey(), move.getValue());
            List<Move> moves = nextNode.getMoves(moveNode);
            moves.forEach(submove -> {
                dice.add(submove.getDie().getValue());
            });
            MoveDto moveDto = new MoveDto(source.getName(), destination.getName(), dice);
            availableMoves.add(moveDto);
            /*
            // generate sub moves
            MoveNode childMoveNode = moveNode.find(move.getKey(), move.getValue());
            if(!childMoveNode.isEnd()) {
                generateMoves(childMoveNode, moveDto.getMoves());
            }//*/
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
