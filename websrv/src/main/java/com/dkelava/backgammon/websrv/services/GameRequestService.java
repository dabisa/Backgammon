package com.dkelava.backgammon.websrv.services;

import com.dkelava.backgammon.websrv.domain.GameRequest;
import com.dkelava.backgammon.websrv.domain.Player;
import com.dkelava.backgammon.websrv.exceptions.UnprocessableEntityException;
import com.dkelava.backgammon.websrv.repo.GameRequestRepository;
import com.dkelava.backgammon.websrv.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRequestService {

    public GameRequest createGameRequest(String challengerName, String opponentName) {
        Player challenger = playerRepository.findByName(challengerName);
        if(challenger == null) {
            throw new UnprocessableEntityException("Player does not exist: " + challengerName);
        }
        Player opponent = playerRepository.findByName(opponentName);
        if(opponent == null) {
            throw new UnprocessableEntityException("Player does not exist: " + opponent);
        }
        GameRequest gameRequest = new GameRequest(challenger, opponent);
        return gameRequestRepository.save(gameRequest);
    }

    public GameRequest getGameRequest(int gameRequestId) {
        return gameRequestRepository.findOne(gameRequestId);
    }

    public GameRequest save(GameRequest gameRequest) {
        return gameRequestRepository.save(gameRequest);
    }

    @Autowired
    private GameRequestRepository gameRequestRepository;
    @Autowired
    private PlayerRepository playerRepository;
}
