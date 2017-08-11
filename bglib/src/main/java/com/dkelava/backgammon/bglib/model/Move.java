package com.dkelava.backgammon.bglib.model;

/**
 * Move represents a move that can be performed on Backgammon board.
 */
public class Move {

    private final Color _color;
    private final Point _source;
    private final Die.Face _die;
    private final boolean _isHit;

    private Move(Color color, Point source, Die.Face die, boolean isHit) {
        _color = color;
        _source = source;
        _die = die;
        _isHit = isHit;
    }

    /**
     * Encodes Move as a string value
     *
     * @return String that represents encoded Move
     */
    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(_isHit ? "H" : "M");
        builder.append(_color.encode());
        builder.append(_source.encode());
        builder.append(_die.encode());
        return builder.toString();
    }

    /**
     * Returns Move that is represented by provided value.
     *
     * @param value Value to be decoded
     * @return Move that represents decoded value
     * @throws Exception when provided value is not a valid code for Move.
     */
    public static Move decode(String value) throws Exception {
        if(value.length() == 5) {
            switch(value.charAt(0)) {
                case 'M':
                    return new Move(Color.decode(value.substring(1,2)), Point.decode(value.substring(2,4)), Die.Face.decode(value.substring(4,5)), false);

                case 'H':
                    return new Move(Color.decode(value.substring(1,2)), Point.decode(value.substring(2,4)), Die.Face.decode(value.substring(4,5)), true);

                default:
                    throw new IllegalArgumentException("invalid move encoding");
            }

        } else {
            throw new IllegalArgumentException("invalid move encoding");
        }
    }

    /**
     * Determines if specified player can perform specified move on given board.
     *
     * @param board Backgammon board
     * @param color Player's color
     * @param source Source point
     * @param die Die to be used in move
     * @returns true If move is possible
     * @returns false If move is not possible
     */
    public static boolean isMovable(Board board, Color color, Point source, Die.Face die) {
        if (color != Color.None) {
            if (source != Point.getBar(color)) {
                if (!board.getPoint(Point.getBar(color)).isEmpty()) {
                    return false;
                }
            }
            if(source == Point.WhiteHome || source == Point.BlackHome) {
                return false;
            }
            if (!board.getPoint(source).isOwner(color)) {
                return false;
            }
            Point destination = source.advance(color, die.getValue());
            if (destination == Point.getHome(color)) {
                if (board.canBearOff(color)) {
                    int distance = source.distance(destination);
                    if (distance < die.getValue() && distance > 0) {
                        for (Point point = Point.getPoint(color, 19); point != source; point = point.advance(color, 1)) {
                            Point destination2 = point.advance(color, die.getValue());
                            if (board.getPoint(point).isOwner(color) && board.getPoint(destination2).isAvailable(color)) {
                                return false;
                            }
                        }
                    }
                } else {
                    return false;
                }
            }
            PointState destinationValue = board.getPoint(destination);
            return destinationValue.isAvailable(color);
        } else {
            return false;
        }
    }

    public static Move createMove(Color color, Point source, Die.Face die, boolean isHit) {
        return new Move(color, source, die, isHit);
    }

    /**
     * Creates move for specified player, source point and die
     */
    public static Move createMove(Board board, Color color, Point source, Die.Face die) {
        Point destination = source.advance(color, die.getValue());
        PointState value = board.getPoint(destination);
        boolean isHit = value.isOwner(color.getOpponent()) && value.isBlot();
        return new Move(color, source, die, isHit);
    }

    /**
     * Returns color of player that generated move
     */
    public final Color getColor() {
        return _color;
    }

    /**
     * Return source point of move
     */
    public final Point getSource() {
        return _source;
    }

    /**
     * Return destination point of move
     */
    public final Point getDestination() {
        return _source.advance(_color, _die.getValue());
    }

    /**
     * Return die used in move
     */
    public final Die.Face getDie() {
        return _die;
    }

    /**
     * Determines if opponents checker is hit
     * @return
     */
    public final boolean isHit() {
        return _isHit;
    }

    /**
     * Returns distance between source and destination points
     */
    public final int getDistance() {
        return getDestination().distance(getSource());
    }

    /**
     * Returns difference between die value and dictance between source and destination.
     */
    public final int getWaste() {
        return _die.getValue() - getDistance();
    }

    /**
     * Determines if move can be performed on given board
     * @returns true If move can be performed
     * @returns false If move can not be performed
     */
    public final boolean canExecute(Board board) {
        if (isMovable(board, getColor(), getSource(), getDie())) {
            PointState destinationValue = board.getPoint(getDestination());
            return (isHit() == (destinationValue.isBlot() && destinationValue.isOwner(getColor().getOpponent())));
        }
        return false;
    }

    /**
     * Determines if move can be undone on given board
     * @returns true Move can be undone
     * @returns false Move can not be undone
     */
    public final boolean canRollback(Board board) {
        PointState destinationValue = board.getPoint(getDestination());
        if (!destinationValue.isOwner(getColor())) {
            return false;
        }

        Point opponentBar = Point.getBar(getColor().getOpponent());
        PointState opponentBarValue = board.getPoint(opponentBar);
        if (isHit() && !opponentBarValue.isOwner(getColor().getOpponent())) {
            return false;
        }

        PointState sourceValue = board.getPoint(getSource());
        return !sourceValue.isOwner(getColor().getOpponent());
    }

    /**
     * Executes move on given board
     */
    public final void execute(Board board) {
        assert (canExecute(board));
        board.removeChecker(getSource(), getColor());
        if (isHit()) {
            Point opponentBar = Point.getBar(getColor().getOpponent());
            board.removeChecker(getDestination(), getColor().getOpponent());
            board.addChecker(opponentBar, getColor().getOpponent());
        }
        board.addChecker(getDestination(), getColor());
    }

    /**
     * Undoes move on given board
     */
    public final void rollback(Board board) {
        assert (canRollback(board));
        board.removeChecker(getDestination(), getColor());
        if (isHit()) {
            Point opponentBar = Point.getBar(getColor().getOpponent());
            board.removeChecker(opponentBar, getColor().getOpponent());
            board.addChecker(getDestination(), getColor().getOpponent());
        }
        board.addChecker(getSource(), getColor());
    }
}
