package com.dkelava.backgammon.websrv.domain;

import javax.persistence.*;

@Entity
public class Game {

    protected Game() {
    }

    public Game(Player whitePlayer, Player blackPlayer, String state) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String state;

    @ManyToOne
    private Player whitePlayer;

    @ManyToOne
    private Player blackPlayer;
}
