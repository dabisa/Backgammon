package com.dkelava.backgammon.websrv.web;

import com.dkelava.backgammon.bglib.model.*;
import com.dkelava.backgammon.websrv.domain.Game;
import com.dkelava.backgammon.websrv.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Dabisa on 12/08/2017.
 */
@RestController
@RequestMapping(path = "/games/{gameId}")
public class GameControler {

    private RandomDieStrategy randomDieStrategy = new RandomDieStrategy();

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping(method = RequestMethod.POST, path = "/move")
    @ResponseStatus(HttpStatus.CREATED)
    public void move(@PathVariable(value = "gameId") int gameId, @RequestBody Map<String, String> json) {
        Game game = gameRepository.findOne(gameId);
        String state = game.getState();
        Backgammon backgammon = new Backgammon();
        try {
            backgammon.restore(state);
            Point source = Point.decode(json.get("source"));
            Point destination = Point.decode(json.get("destination"));
            if(backgammon.getState().getStatus() == Status.Moving && backgammon.getState().getMoves().isMovable(source, destination)) {
                backgammon.move(source, destination);
            } else {
                throw new RuntimeException("Invalid move");
            }
            game.setState(backgammon.encode());
            gameRepository.save(game);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/roll")
    @ResponseStatus(HttpStatus.CREATED)
    public void roll(@PathVariable(value = "gameId") int gameId) {
        Game game = gameRepository.findOne(gameId);
        String state = game.getState();
        Backgammon backgammon = new Backgammon();
        try {
            backgammon.restore(state);
            if(backgammon.getState().getStatus() == Status.Rolling || backgammon.getState().getStatus() == Status.Initial) {
                backgammon.roll(randomDieStrategy.roll(), randomDieStrategy.roll());
            } else {
                throw new RuntimeException("Roll not allowed");
            }
            game.setState(backgammon.encode());
            gameRepository.save(game);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
