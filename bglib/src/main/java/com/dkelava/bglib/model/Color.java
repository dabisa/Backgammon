package com.dkelava.bglib.model;

/**
 * Color is an enumaration of player color
 */
public enum Color {

    /**
     * Represents non defined player
     */
    None,

    /**
     * Represents white player
     */
    White,

    /**
     * Represents black player
     */
    Black;

    private static final String codeNone = "N";
    private static final String codeWhite = "W";
    private static final String codeBlack = "B";

    /**
     * Returns Color that is represented by provided value.
     *
     * @param value Value to be decoded
     * @return Status that represents decoded value
     * @throws Exception when provided value is not a valid code for Status.
     */
    public static Color decode(String value) throws Exception {
        switch(value) {
            case codeNone: return Color.None;
            case codeWhite: return Color.White;
            case codeBlack: return Color.Black;
            default: throw new IllegalArgumentException("invalid color code");
        }
    }

    /**
     * Encodes Color as a string value
     *
     * @return String that represents encoded Color
     */
    public String encode() {
        switch (this) {
            case None: return codeNone;
            case White: return codeWhite;
            case Black: return codeBlack;
            default: return "X";
        }
    }

    /**
     * Returns opponent's color
     *
     * @return Color that represents opponent
     */
    public Color getOpponent() {
        switch (this) {
            case White:
                return Black;
            case Black:
                return White;
            default:
                return None;
        }
    }
}
