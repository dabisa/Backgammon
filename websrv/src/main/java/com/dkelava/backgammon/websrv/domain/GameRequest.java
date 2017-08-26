package com.dkelava.backgammon.websrv.domain;

import javax.persistence.*;

@Entity
public final class GameRequest {

    // Not used but required by JPA
    private GameRequest() {
    }

    public GameRequest(Player challenger, Player opponent) {
        this.challenger = challenger;
        this.opponent = opponent;
        this.game = null;
    }

    public final int getId() {
        return id;
    }

    public final Player getChallenger() {
        return challenger;
    }

    public final Player getOpponent() {
        return opponent;
    }

    public final Game getGame() {
        return game;
    }

    public final void setGame(Game game) {
        this.game = game;
    }

    public final boolean isAccepted() {
        return this.game != null;
    }

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Player challenger;

    @ManyToOne
    private Player opponent;

    @OneToOne
    private Game game;
}
