package com.dkelava.backgammon.websrv.resources;

/**
 * Created by Dabisa on 07/09/2017.
 */
public class DieDto {
    public DieDto(int die, int moves) {
        this.die = die;
        this.moves = moves;
    }

    public int die;
    public int moves;
}
