package com.dkelava.backgammon.websrv.resources;

import com.dkelava.backgammon.bglib.model.Point;

import java.util.HashMap;
import java.util.Map;

public enum PointId {
    WhiteBar("WB", Point.WhiteBar),
    BlackBar("BB", Point.BlackBar),
    Point1("01", Point.Point1),
    Point2("02", Point.Point2),
    Point3("03", Point.Point3),
    Point4("04", Point.Point4),
    Point5("05", Point.Point5),
    Point6("06", Point.Point6),
    Point7("07", Point.Point7),
    Point8("08", Point.Point8),
    Point9("09", Point.Point9),
    Point10("10", Point.Point10),
    Point11("11", Point.Point11),
    Point12("12", Point.Point12),
    Point13("13", Point.Point13),
    Point14("14", Point.Point14),
    Point15("15", Point.Point15),
    Point16("16", Point.Point16),
    Point17("17", Point.Point17),
    Point18("18", Point.Point18),
    Point19("19", Point.Point19),
    Point20("20", Point.Point20),
    Point21("21", Point.Point21),
    Point22("22", Point.Point22),
    Point23("23", Point.Point23),
    Point24("24", Point.Point24),
    WhiteHome("WH", Point.WhiteHome),
    BlackHome("BH", Point.BlackHome);

    public Point create() {
        return point;
    }

    public static PointId parse(String name) {
        return myMap.get(name);
    }

    PointId(String name, Point point) {
        this.name = name;
        this.point = point;
    }

    private static final Map<String, PointId> myMap = new HashMap<>();
    static
    {
        for(PointId pointId : PointId.values()) {
            myMap.put(pointId.name, pointId);
        }
    }

    private final String name;
    private final Point point;
}
