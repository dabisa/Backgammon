package com.dkelava.backgammon.bglib.model;

/**
 * Enumeration of all possible point states.
 * A point can be empty or contain between 1 or 15 checkers of either player's checkers.
 */
public enum PointState {

    Empty(Color.None, 0),
    White1(Color.White, 1),
    White2(Color.White, 2),
    White3(Color.White, 3),
    White4(Color.White, 4),
    White5(Color.White, 5),
    White6(Color.White, 6),
    White7(Color.White, 7),
    White8(Color.White, 8),
    White9(Color.White, 9),
    White10(Color.White, 10),
    White11(Color.White, 11),
    White12(Color.White, 12),
    White13(Color.White, 13),
    White14(Color.White, 14),
    White15(Color.White, 15),
    Black1(Color.Black, 1),
    Black2(Color.Black, 2),
    Black3(Color.Black, 3),
    Black4(Color.Black, 4),
    Black5(Color.Black, 5),
    Black6(Color.Black, 6),
    Black7(Color.Black, 7),
    Black8(Color.Black, 8),
    Black9(Color.Black, 9),
    Black10(Color.Black, 10),
    Black11(Color.Black, 11),
    Black12(Color.Black, 12),
    Black13(Color.Black, 13),
    Black14(Color.Black, 14),
    Black15(Color.Black, 15);

    private final static PointState whitePoints[] = {White1, White2, White3, White4, White5, White6, White7, White8, White9, White10, White11, White12, White13, White14, White15};
    private final static PointState blackPoints[] = {Black1, Black2, Black3, Black4, Black5, Black6, Black7, Black8, Black9, Black10, Black11, Black12, Black13, Black14, Black15};
    private final Color color;
    private final byte quantity;

    PointState(Color color, int quantity) {
        this.color = color;
        this.quantity = (byte) quantity;
    }

    /**
     * Returns PointState that is represented by provided value.
     *
     * @param value Value to be decoded
     * @return PointState that represents decoded value
     * @throws Exception when provided value is not a valid code for PointState.
     */
    public static PointState decode(String value) throws Exception {
        if(value.length() == 3) {
            String color = value.substring(0, 1);
            int quantity = Integer.parseInt(value.substring(1, 3));
            return valueOf(Color.decode(color), quantity);
        } else {
            throw new IllegalArgumentException("invalid point state encoding: " + value);
        }
    }

    /**
     * Returns PointState that corresponds to given player color and quantity
     *
     * @param color Player's color
     * @param quantity Number of checkers
     * @return PointState that corresponds to given input
     */
    public static PointState valueOf(Color color, int quantity) {
        switch (color) {

            case White:
                if (quantity > 0 && quantity <= whitePoints.length) {
                    return whitePoints[quantity - 1];
                } else {
                    throw new IllegalArgumentException("It is illegal to have " + quantity + " of checkers on board");
                }

            case Black:
                if (quantity > 0 && quantity <= blackPoints.length) {
                    return blackPoints[quantity - 1];
                } else {
                    throw new IllegalArgumentException("It is illegal to have " + quantity + " of checkers on board");
                }

            default:
            case None:
                if (quantity != 0) {
                    throw new IllegalArgumentException("Point without owner must have zero checkers");
                }
                return Empty;
        }
    }

    /**
     * Encodes PointState as a string value.
     *
     * @return String that represents encoded PointState
     */
    public String encode() {
        return String.format("%s%02d", getOwner().encode(), getQuantity());
    }

    public final PointState add(Color color) {
        if (color == Color.None) {
            throw new IllegalArgumentException(color + " is not allowed");
        } else if (color == this.color.getOpponent()) {
            throw new IllegalArgumentException("It is not allowed to add checker of color " + color + " to point with state " + this);
        }
        return valueOf(color, quantity + 1);
    }

    /**
     * Returns point state after removal of single checker of specified color
     * @param color Checker color
     * @return PointState after removal of checker
     */
    public final PointState remove(Color color) {
        if (color == Color.None) {
            throw new IllegalArgumentException(color + " is not allowed");
        } else if (this.color != color) {
            throw new IllegalArgumentException("It is not allowed to remove checker of color " + color + " from point with state " + this);
        }

        if (quantity - 1 == 0) {
            return PointState.Empty;
        } else {
            return valueOf(this.color, quantity - 1);
        }
    }

    /**
     * Returns color of point's owner
     * Owner is a player that has at least one checker on given point.
     */
    public final Color getOwner() {
        return this.color;
    }

    /**
     * Determines if point is empty.
     *
     * @returns true If point is empty.
     * @returns false If point contains at least one checker.
     */
    public final boolean isEmpty() {
        return this == PointState.Empty;
    }

    /**
     * Determines if point is owned by player of given color
     *
     * @param color
     * @returns true If point contains checkers of specified color
     * @returns false If point does not contain checkers of specified color
     */
    public final boolean isOwner(Color color) {
        return color == this.color;
    }

    /**
     * Determines if point is a blot (contains single checker of either color).
     *
     * @returns true If point is a blot
     * @returns false If point is not a blot
     */
    public final boolean isBlot() {
        return this.quantity == 1;
    }

    /**
     * Determines if point can be hit by checker of specified color
     *
     * @param color Checker color
     * @returns true If point can be hit
     * @returns true If point can not be hit
     */
    public final boolean canHit(Color color) {
        return this.quantity == 1 && this.color == color.getOpponent();
    }

    /**
     * Determines if checker of specified color can land on this point
     *
     * @param color Checker color
     * @returns true If checker of specified color can land on point
     * @returns true If checker of specified color can not land on point
     */
    public final boolean isAvailable(Color color) {
        if (color == Color.None) {
            throw new IllegalArgumentException(color + " is not allowed");
        }
        return isEmpty() || isOwner(color) || isBlot();
    }

    /**
     * Returns number of checker on point
     *
     * @return Integer representing number of checkers on point
     */
    public final int getQuantity() {
        return this.quantity;
    }
}
