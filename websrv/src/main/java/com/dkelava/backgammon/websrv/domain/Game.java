package com.dkelava.backgammon.websrv.domain;

import com.dkelava.backgammon.bglib.model.Backgammon;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Dabisa on 11/08/2017.
 */
@Entity
public class Game {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String state;

    /*
    @Column
    private List<String> history;
    */

    @ManyToOne
    private Player playerOne;

    @ManyToOne
    private Player playerTwo;

    protected Game() {
    }

    public Game(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.state = new Backgammon().encode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }
}
