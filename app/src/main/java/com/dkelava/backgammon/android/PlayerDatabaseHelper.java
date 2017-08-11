package com.dkelava.backgammon.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dabisa on 20.3.2017..
 */

public class PlayerDatabaseHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PlayerDatabaseContract.Players.TABLE_NAME + " (" +
                    PlayerDatabaseContract.Players._ID + " INTEGER PRIMARY KEY," +
                    PlayerDatabaseContract.Players.COLUMN_NAME_TITLE + " TEXT," +
                    PlayerDatabaseContract.Players.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    PlayerDatabaseContract.Players.COLUMN_TYPE + " TEXT," +
                    PlayerDatabaseContract.Players.COLUMN_RESOURCE + " INTEGER," +
                    PlayerDatabaseContract.Players.COLUMN_NAME_NO_GAMES + " INTEGER, " +
                    PlayerDatabaseContract.Players.COLUMN_NAME_NO_WINS + " INTEGER, " +
                    PlayerDatabaseContract.Players.COLUMN_NAME_POINTS + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PlayerDatabaseContract.Players.TABLE_NAME;

    private String[] columns = {
            PlayerDatabaseContract.Players._ID,
            PlayerDatabaseContract.Players.COLUMN_NAME_TITLE,
            PlayerDatabaseContract.Players.COLUMN_NAME_DESCRIPTION,
            PlayerDatabaseContract.Players.COLUMN_TYPE,
            PlayerDatabaseContract.Players.COLUMN_RESOURCE,
            PlayerDatabaseContract.Players.COLUMN_NAME_NO_GAMES,
            PlayerDatabaseContract.Players.COLUMN_NAME_NO_WINS,
            PlayerDatabaseContract.Players.COLUMN_NAME_POINTS
    };


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Players.db";

    public PlayerDatabaseHelper(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        add(db, new PlayerInfo("Human", "Human player", PlayerInfo.Type.Human, 0, 0, 0, 0));
        add(db, new PlayerInfo("Floyd", "Trained Neuran Network", PlayerInfo.Type.NeuralNetwork, R.raw.nn, 0, 0, 0));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public PlayerInfo getPlayerInfo(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = PlayerDatabaseContract.Players.COLUMN_NAME_TITLE + "=?";
        String[] selectionArgs = { name };

        String orderBy = PlayerDatabaseContract.Players.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                PlayerDatabaseContract.Players.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null//orderBy
        );
        boolean b = cursor.moveToFirst();
        if(b) {
            return PlayerInfo.load(cursor);
        } else {
            return null;
        }
    }

    public Cursor getAllPlayers() {
        SQLiteDatabase db = this.getReadableDatabase();

        String orderBy = PlayerDatabaseContract.Players.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                PlayerDatabaseContract.Players.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                orderBy
        );

        int count = cursor.getCount();

        return cursor;
    }

    private void add(SQLiteDatabase db, PlayerInfo info) {
        ContentValues values = new ContentValues();
        values.put(PlayerDatabaseContract.Players.COLUMN_NAME_TITLE, info.title);
        values.put(PlayerDatabaseContract.Players.COLUMN_NAME_DESCRIPTION, info.description);
        values.put(PlayerDatabaseContract.Players.COLUMN_TYPE, info.type.name());
        values.put(PlayerDatabaseContract.Players.COLUMN_RESOURCE, info.resource);
        values.put(PlayerDatabaseContract.Players.COLUMN_NAME_NO_GAMES, info.totalGamesPlayed);
        values.put(PlayerDatabaseContract.Players.COLUMN_NAME_NO_WINS, info.totalGamesWon);
        values.put(PlayerDatabaseContract.Players.COLUMN_NAME_POINTS, info.totalPoints);
        long newRowId = db.insert(PlayerDatabaseContract.Players.TABLE_NAME, null, values);
    }
}
