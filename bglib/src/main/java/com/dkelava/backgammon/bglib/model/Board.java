package com.dkelava.backgammon.bglib.model;

import com.google.common.base.Splitter;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Represents Backgammon board.
 *
 * @note This class in mutable!
 */
public class Board {

    private PointState[] points = new PointState[Point.values().length];

    /**
     * Default constructor creates board without checkers (empty board).
     */
    public Board() {
        for (Point point : Point.values()) {
            setPoint(point, PointState.Empty);
        }
    }

    /**
     * Copy constructor
     */
    public Board(Board orig) {
        for (Point point : Point.values()) {
            setPoint(point, orig.getPoint(point));
        }
    }

    /**
     * Creates Board that is represented by given String value
     *
     * @param value Value to be decoded
     * @return Board that is represented by given value
     * @throws Exception when provided value is not a valid code for Board.
     */
    public static Board decode(String value) throws Exception {
        Board board = new Board();
        board.restore(value);
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Board) {
            Board other = (Board) o;
            for (Point point : Point.values()) {
                if (this.points[point.ordinal()] != other.points[point.ordinal()]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(points);
    }

    /**
     * Restores board using board state encoded in String
     */
    public void restore(String gameState) throws Exception {
        Iterable<String> pointsValues = Splitter.fixedLength(3).split(gameState);
        Iterator<String> it = pointsValues.iterator();

        for (Point point : Point.values()) {
            if (it.hasNext()) {
                setPoint(point, getPoint(point).decode(it.next()));
            } else {
                throw new IllegalArgumentException("invalid board encoding: " + gameState + " at point " + point);
            }
        }
    }

    /**
     * Encodes Board state as a string value.
     *
     * @return String that represents encoded Board state
     */
    public String encode() {
        StringBuilder builder = new StringBuilder();
        for (Point point : Point.values()) {
            builder.append(getPoint(point).encode());
            //builder.append("-");
        }
        return builder.toString();
    }

    /**
     * Removes all checkers from board
     */
    public final void clear() {
        for (Point point : Point.values()) {
            setPoint(point, PointState.Empty);
        }
    }

    /**
     * Initialize board to Backgammon starting position.
     */
    public final void initialize() {
        clear();
        setPoint(Point.Point1, PointState.White2);
        setPoint(Point.Point6, PointState.Black5);
        setPoint(Point.Point8, PointState.Black3);
        setPoint(Point.Point12, PointState.White5);
        setPoint(Point.Point13, PointState.Black5);
        setPoint(Point.Point17, PointState.White3);
        setPoint(Point.Point19, PointState.White5);
        setPoint(Point.Point24, PointState.Black2);
    }

    /**
     * Retrieves state for specified Point
     */
    public final PointState getPoint(Point point) {
        return points[point.ordinal()];
    }

    /**
     * Determines if player of specified color can bear off his checkers.
     */
    public final boolean canBearOff(Color color) {
        if (color == Color.None) {
            throw new IllegalArgumentException("Color.None is not allowed");
        }

        for (int i = 0; i <= 18; ++i) {
            PointState val = getPoint(Point.getPoint(color, i));
            if (val.isOwner(color)) {
                return false;
            }
        }

        int countOnHomeBoard = 0;
        for (int i = 19; i <= 24; ++i) {
            PointState val = getPoint(Point.getPoint(color, i));
            if (val.isOwner(color)) {
                countOnHomeBoard += val.getQuantity();
            }
        }
        int countOnHome = getPoint(Point.getHome(color)).getQuantity();

        return (countOnHomeBoard + countOnHome) == 15 && countOnHomeBoard > 0;
    }

    /**
     * Determines if board state is legal according to Backgammon rules.
     */
    public final boolean isLegal() {

        if (getPoint(Point.WhiteBar).getOwner() == Color.Black) {
            return false;
        }

        if (getPoint(Point.WhiteHome).getOwner() == Color.Black) {
            return false;
        }

        if (getPoint(Point.BlackBar).getOwner() == Color.White) {
            return false;
        }

        if (getPoint(Point.BlackHome).getOwner() == Color.White) {
            return false;
        }

        int whiteCheckers = 0;
        int blackCheckers = 0;

        for (PointState pointState : points) {
            switch (pointState.getOwner()) {
                case White:
                    whiteCheckers += pointState.getQuantity();
                    break;
                case Black:
                    blackCheckers += pointState.getQuantity();
                    break;
            }
        }
        return (whiteCheckers == 15 && blackCheckers == 15);
    }

    /**
     * Determines if gammon conditions are satisfied.
     */
    boolean isGammon() {
        return (getPoint(Point.WhiteHome) == PointState.White15 && getPoint(Point.BlackHome).getOwner() != Color.Black) ||
                (getPoint(Point.BlackHome) == PointState.Black15 && getPoint(Point.WhiteHome).getOwner() != Color.White);
    }

    /**
     * Determines if end conditions are satisfied.
     */
    public final boolean isEnd() {
        return getPoint(Point.WhiteHome) == PointState.White15 || getPoint(Point.BlackHome) == PointState.Black15;
    }

    /**
     * Set state of specified point.
     */
    public final void setPoint(Point point, PointState value) {
        points[point.ordinal()] = value;
    }

    /**
     * Add checker of specified color on specified point.
     */
    public final PointState addChecker(Point point, Color color) {
        if (!getPoint(point).isAvailable(color)) {
            throw new IllegalArgumentException("Point is not available to player of color " + color);
        } else if (getPoint(point).getQuantity() == 15) {
            throw new IllegalArgumentException("Point is full");
        }

        setPoint(point, getPoint(point).add(color));
        return getPoint(point);
    }

    /**
     * Removes checker of specified color from specified point
     */
    public final PointState removeChecker(Point point, Color color) {
        if (!getPoint(point).isOwner(color)) {
            throw new IllegalArgumentException("Player of color " + color + " is not the owner of point");
        }
        setPoint(point, getPoint(point).remove(color));
        return getPoint(point);
    }
}
