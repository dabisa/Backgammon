package com.dkelava.backgammon.websrv.web;

/**
 * Created by Dabisa on 17/08/2017.
 */
public class GameDescriptorDto {

    private final String player;
    private final String opponent;

    public GameDescriptorDto(String player, String opponent) {
        this.player = player;
        this.opponent = opponent;
    }

    public final String getPlayer() {
        return player;
    }

    public final String getOpponent() {
        return opponent;
    }
}
