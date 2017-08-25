package com.dkelava.backgammon.websrv.services;

import com.dkelava.backgammon.websrv.domain.Game;
import com.dkelava.backgammon.websrv.domain.Player;
import com.dkelava.backgammon.websrv.exceptions.MissingResourceException;
import com.dkelava.backgammon.websrv.repo.GameRepository;
import com.dkelava.backgammon.websrv.repo.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public Game createGame(String playerOneName, String playerTwoName, String state) {
        Player playerOne = playerRepository.findByName(playerOneName);
        if(playerOne == null) {
            throw new RuntimeException("Player does not exist: " + playerOneName);
        }
        Player playerTwo = playerRepository.findByName(playerTwoName);
        if(playerTwo == null) {
            throw new RuntimeException("Player does not exist: " + playerTwoName);
        }
        Game game = new Game(playerOne, playerTwo, state);
        return gameRepository.save(game);
    }

    public Game getGame(int gameId) {
        Game game = this.gameRepository.findOne(gameId);
        if(game != null) {
            return game;
        } else {
            throw new MissingResourceException("Game does not exist: " + gameId);
        }
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }
}
