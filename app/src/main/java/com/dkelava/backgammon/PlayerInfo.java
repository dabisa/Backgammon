package com.dkelava.backgammon;

import android.database.Cursor;

public class PlayerInfo {
    public String title;
    public String description;
    public Type type;
    public int totalGamesPlayed;
    public int totalGamesWon;
    public int totalPoints;
    public int resource;

    public static PlayerInfo load(Cursor cursor) {
        int titleIndex = cursor.getColumnIndex(PlayerDatabaseContract.Players.COLUMN_NAME_TITLE);
        int descriptionIndex = cursor.getColumnIndex(PlayerDatabaseContract.Players.COLUMN_NAME_DESCRIPTION);
        int typeIndex = cursor.getColumnIndex(PlayerDatabaseContract.Players.COLUMN_TYPE);
        int noGamesIndex = cursor.getColumnIndex(PlayerDatabaseContract.Players.COLUMN_NAME_NO_GAMES);
        int noWinsIndex = cursor.getColumnIndex(PlayerDatabaseContract.Players.COLUMN_NAME_NO_WINS);
        int pointsIndex = cursor.getColumnIndex(PlayerDatabaseContract.Players.COLUMN_NAME_POINTS);
        int resourceIndex = cursor.getColumnIndex(PlayerDatabaseContract.Players.COLUMN_RESOURCE);

        String title = cursor.getString(titleIndex);
        String description = cursor.getString(descriptionIndex);
        String type = cursor.getString(typeIndex);
        int noGames = cursor.getInt(noGamesIndex);
        int noWins = cursor.getInt(noWinsIndex);
        int points = cursor.getInt(pointsIndex);
        int resource = cursor.getInt(resourceIndex);

        return new PlayerInfo(title, description, PlayerInfo.Type.valueOf(type), resource, noGames, noWins, points);
    }

    public PlayerInfo() {
        title = "";
        description = "";
        type = Type.Human;
    }

    public PlayerInfo(String title, String description, Type type, int resource) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.resource = resource;
    }

    public PlayerInfo(String title, String description, Type type, int resource, int totalGamesPlayed, int totalGamesWon, int totalPoints) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.resource = resource;
        this.totalGamesPlayed = totalGamesPlayed;
        this.totalGamesWon = totalGamesWon;
        this.totalPoints = totalPoints;
    }

    public int getIconResource() {
        switch (type) {
            case Human: return R.drawable.human;
            default: return R.drawable.droid;
        }
    }

    public enum Type {
        Human,
        NeuralNetwork
    }
}
