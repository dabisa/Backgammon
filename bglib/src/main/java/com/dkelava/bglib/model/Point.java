package com.dkelava.bglib.model;

/**
 * Point is enumeration of all points on Backgammon board.
 */
public enum Point {

    WhiteBar(Color.White, 0),
    BlackBar(Color.Black, 25),
    Point1(Color.None, 1),
    Point2(Color.None, 2),
    Point3(Color.None, 3),
    Point4(Color.None, 4),
    Point5(Color.None, 5),
    Point6(Color.None, 6),
    Point7(Color.None, 7),
    Point8(Color.None, 8),
    Point9(Color.None, 9),
    Point10(Color.None, 10),
    Point11(Color.None, 11),
    Point12(Color.None, 12),
    Point13(Color.None, 13),
    Point14(Color.None, 14),
    Point15(Color.None, 15),
    Point16(Color.None, 16),
    Point17(Color.None, 17),
    Point18(Color.None, 18),
    Point19(Color.None, 19),
    Point20(Color.None, 20),
    Point21(Color.None, 21),
    Point22(Color.None, 22),
    Point23(Color.None, 23),
    Point24(Color.None, 24),
    WhiteHome(Color.White, 25),
    BlackHome(Color.Black, 0);

    private final static int BAR_INDEX = 0;
    private final static int HOME_INDEX = 25;
    private final static Point[] whitePoints = {WhiteBar, Point1, Point2, Point3, Point4, Point5, Point6, Point7, Point8, Point9, Point10, Point11, Point12, Point13, Point14, Point15, Point16, Point17, Point18, Point19, Point20, Point21, Point22, Point23, Point24, WhiteHome};
    private final static Point[] blackPoints = {BlackBar, Point24, Point23, Point22, Point21, Point20, Point19, Point18, Point17, Point16, Point15, Point14, Point13, Point12, Point11, Point10, Point9, Point8, Point7, Point6, Point5, Point4, Point3, Point2, Point1, BlackHome};

    private static final String codeWhiteBar = "WB";
    private static final String codeBlackBar = "BB";
    private static final String codeWhiteHome = "WH";
    private static final String codeBlackHome = "BH";
    private static final String codePoint1 = "01";
    private static final String codePoint2 = "02";
    private static final String codePoint3 = "03";
    private static final String codePoint4 = "04";
    private static final String codePoint5 = "05";
    private static final String codePoint6 = "06";
    private static final String codePoint7 = "07";
    private static final String codePoint8 = "08";
    private static final String codePoint9 = "09";
    private static final String codePoint10 = "10";
    private static final String codePoint11 = "11";
    private static final String codePoint12 = "12";
    private static final String codePoint13 = "13";
    private static final String codePoint14 = "14";
    private static final String codePoint15 = "15";
    private static final String codePoint16 = "16";
    private static final String codePoint17 = "17";
    private static final String codePoint18 = "18";
    private static final String codePoint19 = "19";
    private static final String codePoint20 = "20";
    private static final String codePoint21 = "21";
    private static final String codePoint22 = "22";
    private static final String codePoint23 = "23";
    private static final String codePoint24 = "24";

    private final Color exclusiveOwner;
    private final byte index;

    Point(Color exclusiveOwner, int index) {
        this.exclusiveOwner = exclusiveOwner;
        this.index = (byte) index;
    }

    /**
     * Returns Point that is represented by given String value
     *
     * @param value Value to be decoded
     * @return Point that is represented by given value
     * @throws Exception when provided value is not a valid code for Point.
     */
    public static Point decode(String value) throws Exception {
        switch (value) {
            case codeWhiteBar:
                return WhiteBar;
            case codeBlackBar:
                return BlackBar;
            case codeWhiteHome:
                return WhiteHome;
            case codeBlackHome:
                return BlackHome;
            case codePoint1:
                return Point1;
            case codePoint2:
                return Point2;
            case codePoint3:
                return Point3;
            case codePoint4:
                return Point4;
            case codePoint5:
                return Point5;
            case codePoint6:
                return Point6;
            case codePoint7:
                return Point7;
            case codePoint8:
                return Point8;
            case codePoint9:
                return Point9;
            case codePoint10:
                return Point10;
            case codePoint11:
                return Point11;
            case codePoint12:
                return Point12;
            case codePoint13:
                return Point13;
            case codePoint14:
                return Point14;
            case codePoint15:
                return Point15;
            case codePoint16:
                return Point16;
            case codePoint17:
                return Point17;
            case codePoint18:
                return Point18;
            case codePoint19:
                return Point19;
            case codePoint20:
                return Point20;
            case codePoint21:
                return Point21;
            case codePoint22:
                return Point22;
            case codePoint23:
                return Point23;
            case codePoint24:
                return Point24;
            default:
                throw new IllegalArgumentException("invalid point code");
        }
    }

    /**
     * Returns bar for given player color
     *
     * @param color Players color
     * @return Point that represents bar for given player
     */
    public static Point getBar(Color color) {
        switch (color) {
            case White:
                return WhiteBar;
            case Black:
                return BlackBar;
            default:
                throw new IllegalArgumentException(color + " is not allowed");
        }
    }

    /**
     * Returns home for given player color
     *
     * @param color Players color
     * @return Point that represents home for given player
     */
    public static Point getHome(Color color) {
        switch (color) {
            case White:
                return WhiteHome;
            case Black:
                return BlackHome;
            default:
                throw new IllegalArgumentException(color + " is not allowed");
        }
    }

    /**
     * Returns point for given player's color and index.
     *
     * @param color Players color
     * @param index Index of point (0 - bar, 25, - home, 1-24 - all other points)
     * @return Point that is represents by given parameters
     */
    public static Point getPoint(Color color, int index) {
        switch (color) {
            case White:
                if (BAR_INDEX <= index && index <= HOME_INDEX) {
                    return whitePoints[index];
                } else {
                    throw new IllegalArgumentException("Index " + index + " is not allowed");
                }
            case Black:
                if (BAR_INDEX <= index && index <= HOME_INDEX) {
                    return blackPoints[index];
                } else {
                    throw new IllegalArgumentException("Index " + index + " is not allowed");
                }
            default:
                throw new IllegalArgumentException(color + " is not allowed");
        }
    }

    /**
     * Returns all points for given player's color sorted by point number.
     *
     * @param color Player's color
     * @return Array of points sorted by point number for given player's color.
     */
    public static Point[] getPoints(Color color) {
        switch (color) {
            case White:
                return whitePoints;
            case Black:
                return blackPoints;
            default:
                throw new IllegalArgumentException(color + " is not allowed");
        }
    }

    /**
     * Encodes Point as a string value.
     *
     * @return String that represents encoded Point
     */
    public String encode() {
        switch (this) {
            case WhiteBar:
                return codeWhiteBar;
            case BlackBar:
                return codeBlackBar;
            case WhiteHome:
                return codeWhiteHome;
            case BlackHome:
                return codeBlackHome;
            case Point1:
                return codePoint1;
            case Point2:
                return codePoint2;
            case Point3:
                return codePoint3;
            case Point4:
                return codePoint4;
            case Point5:
                return codePoint5;
            case Point6:
                return codePoint6;
            case Point7:
                return codePoint7;
            case Point8:
                return codePoint8;
            case Point9:
                return codePoint9;
            case Point10:
                return codePoint10;
            case Point11:
                return codePoint11;
            case Point12:
                return codePoint12;
            case Point13:
                return codePoint13;
            case Point14:
                return codePoint14;
            case Point15:
                return codePoint15;
            case Point16:
                return codePoint16;
            case Point17:
                return codePoint17;
            case Point18:
                return codePoint18;
            case Point19:
                return codePoint19;
            case Point20:
                return codePoint20;
            case Point21:
                return codePoint21;
            case Point22:
                return codePoint22;
            case Point23:
                return codePoint23;
            case Point24:
                return codePoint24;
            default:
                return "X";
        }
    }

    /**
     * Returns Point where specified player would land if moving for specified distance.
     *
     * @param color    Player's color
     * @param distance Distance between source and destination points
     * @return Destination point
     */
    public final Point advance(Color color, int distance) {
        if (color == Color.None) {
            throw new IllegalArgumentException(color + " is not allowed");
        } else if (this.exclusiveOwner != Color.None && this.exclusiveOwner != color) {
            throw new IllegalArgumentException("Player of color " + color + " can not advance point " + this);
        }

        int relativeIndex = (color != Color.Black ? this.index : 25 - this.index) + distance;
        relativeIndex = Math.max(BAR_INDEX, relativeIndex);
        relativeIndex = Math.min(HOME_INDEX, relativeIndex);

        switch (relativeIndex) {
            case BAR_INDEX:
                return getBar(color);
            case HOME_INDEX:
                return getHome(color);
            default:
                return getPoint(color, relativeIndex);
        }
    }

    /**
     * Returns distance between this and specified point
     *
     * @param other Point for witch we want to calculate distance
     * @return Distance between points
     */
    public final int distance(Point other) {
        if (this.exclusiveOwner == Color.None || other.exclusiveOwner == Color.None || this.exclusiveOwner == other.exclusiveOwner) {
            return Math.abs(this.index - other.index);
        } else {
            throw new IllegalArgumentException("Distance for points " + this + " and " + other + " is not defined");
        }
    }
}
