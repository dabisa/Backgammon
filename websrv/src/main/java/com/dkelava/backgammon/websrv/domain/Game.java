package com.dkelava.backgammon.websrv.domain;

import javax.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String state;

    @ManyToOne
    private Player whitePlayer;

    @ManyToOne
    private Player blackPlayer;

    @Column
    private Boolean whitePlayerAccepted;

    @Column
    private Boolean blackPlayerAccepted;

    @Column
    private int lastAction;

    protected Game() {
    }

    public Game(Player whitePlayer, Player blackPlayer, String state) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.state = state;
        this.whitePlayerAccepted = false;
        this.blackPlayerAccepted = false;
        this.lastAction = 0;
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

    public int getLastAction() {
        return lastAction;
    }

    public void setNextAction() {
        this.lastAction++;
    }

    public void acceptWhite() {
        this.whitePlayerAccepted = true;
    }

    public void acceptBlack() {
        this.blackPlayerAccepted = true;
    }

    public boolean isAcepted() {
        return this.whitePlayerAccepted && this.blackPlayerAccepted;
    }
}
