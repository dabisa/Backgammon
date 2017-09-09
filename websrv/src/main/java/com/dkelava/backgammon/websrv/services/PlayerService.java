package com.dkelava.backgammon.websrv.services;

import com.dkelava.backgammon.bglib.nn.BackgammonNeuralNetwork;
import com.dkelava.backgammon.bglib.nn.SimpleInputCoder;
import com.dkelava.backgammon.bglib.nn.SimpleOutputCoder;
import com.dkelava.backgammon.websrv.domain.Player;
import com.dkelava.backgammon.websrv.domain.PlayerType;
import com.dkelava.backgammon.websrv.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    private final Map<String, BackgammonNeuralNetwork> neuralNetworks = new HashMap<>();

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createAIPlayer(String name, File file) {
        try {
            BackgammonNeuralNetwork nn = BackgammonNeuralNetwork.load(new SimpleInputCoder(), new SimpleOutputCoder(), file);
            neuralNetworks.put(name, nn);
            Player player = new Player(name, PlayerType.AI);
            return playerRepository.save(player);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Player createHumanPlayer(String name) {
        Player player = new Player(name, PlayerType.Human);
        return playerRepository.save(player);
    }

    public Player getPlayer(String name) {
        return playerRepository.findByName(name);
    }

    public BackgammonNeuralNetwork getNeuralNetwork(String playerName) {
        return neuralNetworks.get(playerName);
    }
}
