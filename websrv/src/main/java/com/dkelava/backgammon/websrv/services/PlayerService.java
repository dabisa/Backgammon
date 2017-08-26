package com.dkelava.backgammon.websrv.services;

import com.dkelava.backgammon.websrv.domain.Player;
import com.dkelava.backgammon.websrv.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Dabisa on 11/08/2017.
 */
@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(String name) {
        Player player = new Player(name);
        return playerRepository.save(player);
    }

    public Player getPlayer(String name) {
        return playerRepository.findByName(name);
    }
}
