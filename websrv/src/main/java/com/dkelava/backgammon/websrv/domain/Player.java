package com.dkelava.backgammon.websrv.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

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

    @Enumerated(EnumType.ORDINAL)
    private PlayerType playerType;

    protected Player() {
    }

    public Player(String name, PlayerType playerType) {
        this.name = name;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesDraw = 0;
        this.playerType = playerType;
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

    public PlayerType getPlayerType() {
        return playerType;
    }
}
