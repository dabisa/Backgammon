package com.dkelava.backgammon.bglib.model;

/**
 * Status is an enumeration of all game states. Depending on state player can perform different actions.
 */
public enum Status {

    /**
     * Empty board. Game is not started yet.
     */
    Empty,

    /**
     * State where player is expected to roll initial dice.
     */
    Initial,

    /**
     * State where player is expected to move its pieces.
     */
    Moving,

    /**
     * State where all moves are made. Player is expected to pick up his dice.
     */
    NoMoves,

    /**
     * State at rthe beginning of players turn. Player can roll dice or offer a double if he is owner of the cube.
     */
    Rolling,

    /**
     * State where double is offered. Opponent must respond with 'yes' or 'no.
     */
    DoubleStake,

    /**
     * State after one of the players has resigned. The game is finished.
     */
    Resigned,

    /**
     * State that represent normal end of the game where one player has moved all its checkers in its home board.
     */
    End;

    private static final String codeEmpty = "O";
    private static final String codeInitial = "I";
    private static final String codeMoving = "M";
    private static final String codeNoMoves = "N";
    private static final String codeRolling = "R";
    private static final String codeDoubleStake = "D";
    private static final String codeResigned = "S";
    private static final String codeEnd = "F";

    /**
     * Returns Status that is represented by provided value.
     *
     * @param value Value to be decoded
     * @return Status that represents decoded value
     * @throws Exception when provided value is not a valid code for Status.
     */
    public static Status decode(String value) throws Exception {
        switch (value) {
            case codeEmpty:
                return Empty;
            case codeInitial:
                return Initial;
            case codeMoving:
                return Moving;
            case codeNoMoves:
                return NoMoves;
            case codeRolling:
                return Rolling;
            case codeDoubleStake:
                return DoubleStake;
            case codeResigned:
                return Resigned;
            case codeEnd:
                return End;
            default:
                throw new IllegalArgumentException("invalid status code");
        }
    }

    /**
     * Encodes Status as a string value.
     *
     * @return String that represents encoded Status
     */
    public String encode() {
        switch (this) {
            case Empty:
                return codeEmpty;
            case Initial:
                return codeInitial;
            case Moving:
                return codeMoving;
            case NoMoves:
                return codeNoMoves;
            case Rolling:
                return codeRolling;
            case DoubleStake:
                return codeDoubleStake;
            case Resigned:
                return codeResigned;
            case End:
                return codeEnd;
            default:
                return "X";
        }
    }
}
