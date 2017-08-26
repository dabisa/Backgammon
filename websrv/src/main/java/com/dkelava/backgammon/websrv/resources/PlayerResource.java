package com.dkelava.backgammon.websrv.resources;

import org.springframework.hateoas.ResourceSupport;

public class PlayerResource extends ResourceSupport {

    public PlayerResource(String name, Integer gamesWon, Integer gamesLost, Integer gamesDraw) {
        this.name = name;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesDraw = gamesDraw;
    }

    public String getName() {
        return name;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public Integer getGamesLost() {
        return gamesLost;
    }

    public Integer getGamesDraw() {
        return gamesDraw;
    }

    private String name;
    private Integer gamesWon;
    private Integer gamesLost;
    private Integer gamesDraw;
}
