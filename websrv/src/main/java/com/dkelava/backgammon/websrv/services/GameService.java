package com.dkelava.backgammon.websrv.services;

import com.dkelava.backgammon.websrv.domain.Game;
import com.dkelava.backgammon.websrv.domain.Player;
import com.dkelava.backgammon.websrv.repo.GameRepository;
import com.dkelava.backgammon.websrv.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Dabisa on 11/08/2017.
 */
@Service
public class GameService {
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    protected GameService() {
    }

    @Autowired
    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Game createGame(String playerOneName, String playerTwoName) {
        Player playerOne = playerRepository.findByName(playerOneName);
        if(playerOne == null) {
            throw new RuntimeException("Player does not exist: " + playerOneName);
        }
        Player playerTwo = playerRepository.findByName(playerTwoName);
        if(playerTwo == null) {
            throw new RuntimeException("Player does not exist: " + playerTwoName);
        }
        Game game = new Game(playerOne, playerTwo);
        return gameRepository.save(game);
    }
}
