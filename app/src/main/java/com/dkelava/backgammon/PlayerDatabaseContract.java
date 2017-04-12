package com.dkelava.backgammon;

import android.provider.BaseColumns;

/**
 * Created by Dabisa on 20.3.2017..
 */

public final class PlayerDatabaseContract {
    private PlayerDatabaseContract() {}

    public static class Players implements BaseColumns {
        public static final String TABLE_NAME = "players";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_NAME_NO_GAMES = "no_games";
        public static final String COLUMN_NAME_NO_WINS = "no_wins";
        public static final String COLUMN_NAME_POINTS = "points";
        public static final String COLUMN_RESOURCE = "resource";

    }
}
