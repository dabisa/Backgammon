package com.dkelava.backgammon.websrv.services;

import com.dkelava.backgammon.websrv.domain.Game;
import com.dkelava.backgammon.websrv.domain.Player;
import com.dkelava.backgammon.websrv.exceptions.MissingResourceException;
import com.dkelava.backgammon.websrv.exceptions.UnprocessableEntityException;
import com.dkelava.backgammon.websrv.repo.GameRepository;
import com.dkelava.backgammon.websrv.repo.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public Game createGame(String owner, String whitePlayerName, String blackPlayerName, String state) {
        Player playerOne = playerRepository.findByName(whitePlayerName);
        if(playerOne == null) {
            throw new UnprocessableEntityException("Player does not exist: " + whitePlayerName);
        }
        Player playerTwo = playerRepository.findByName(blackPlayerName);
        if(playerTwo == null) {
            throw new UnprocessableEntityException("Player does not exist: " + blackPlayerName);
        }
        Game game = new Game(playerOne, playerTwo, state);
        if(owner.equals(whitePlayerName)) {
            game.acceptWhite();
        }
        if(owner.equals(blackPlayerName)) {
            game.acceptBlack();
        }
        return gameRepository.save(game);
    }

    public Game acceptGame(int gameId, String playerName) {
        Game game = getGame(gameId);
        boolean accepted = false;
        if(game.getWhitePlayer().getName().equals(playerName)) {
            game.acceptWhite();
            accepted = true;
        }
        if(game.getBlackPlayer().getName().equals(playerName)) {
            game.acceptBlack();
            accepted = true;
        }
        if(!accepted) {
            throw new AccessDeniedException("Not allowed");
        } else {
            return gameRepository.save(game);
        }
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
