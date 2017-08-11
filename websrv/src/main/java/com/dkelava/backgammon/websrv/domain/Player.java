package com.dkelava.backgammon.websrv.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Dabisa on 11/08/2017.
 */
@Entity
public class Player {

    @Id
    private String name;

    @Column
    private Integer gamesWon;

    @Column
    private Integer gamesLost;

    @Column
    private Integer gamesDraw;

    protected Player() {
    }

    public Player(String name) {
        this.name = name;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesDraw = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Integer getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(Integer gamesLost) {
        this.gamesLost = gamesLost;
    }

    public Integer getGamesDraw() {
        return gamesDraw;
    }

    public void setGamesDraw(Integer gamesDraw) {
        this.gamesDraw = gamesDraw;
    }
}
