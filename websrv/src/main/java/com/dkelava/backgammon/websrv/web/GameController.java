package com.dkelava.backgammon.websrv.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.dkelava.backgammon.bglib.model.*;
import com.dkelava.backgammon.websrv.domain.Game;
import com.dkelava.backgammon.websrv.repo.GameRepository;
import com.dkelava.backgammon.websrv.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dabisa on 12/08/2017.
 */
@RestController
@RequestMapping(path = "/games/{gameId}")
public class GameController {

    private RandomDieStrategy randomDieStrategy = new RandomDieStrategy();

    @Autowired
    private RepositoryEntityLinks repositoryEntityLinks;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    private GameResource createResource(BackgammonState backgammonState, int gameId, int actionId) throws Exception {
        GameResource resource = new GameResource(backgammonState);
        resource.add(linkTo(methodOn(GameController.class).getGame(gameId)).withSelfRel());
        switch (backgammonState.getStatus()) {
            case Initial:
            case Rolling:
            case Moving:
            case DoubleStake:
            case NoMoves:
                resource.add(linkTo(methodOn(GameController.class).doAction(gameId, actionId + 1, null)).withRel("nextAction"));
                break;
            default:
                break;
        }
        return resource;
    }

    // CREATE GAME
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createGame(@PathVariable int gameId, @RequestBody GameDescriptorDto gameDescriptorDto) throws Exception {
        Game game = gameService.createGame(gameDescriptorDto.getPlayer(), gameDescriptorDto.getOpponent());
        if(game != null) {
            return ResponseEntity.created(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ACCEPT GAME
    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<?> acceptGame(@PathVariable int gameId, @RequestBody boolean accepted) throws Exception {
        Game game = gameRepository.findOne(gameId);
        if(game != null) {
            game.setAccepted(accepted);
            gameRepository.save(game);
            return ResponseEntity.ok().location(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET GAME STATE
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<GameResource> getGame(@PathVariable int gameId) throws Exception {
        Game game = gameRepository.findOne(gameId);
        if(game != null) {
            Backgammon backgammon = new Backgammon();
            backgammon.restore(game.getState());
            return ResponseEntity.ok(createResource(backgammon.getState(), gameId, game.getLastAction()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ACTION
    @RequestMapping(method = RequestMethod.PUT, path = "/action/{actionId}")
    public ResponseEntity<?> doAction(@PathVariable(value = "gameId") int gameId,
                                  @PathVariable(value = "actionId") int actionId,
                                  @RequestBody ActionDto actionDto) throws Exception {
        Game game = gameRepository.findOne(gameId);
        if(game != null ) {
            // check if action is next action
            if(actionId == game.getLastAction() + 1) {
                String state = game.getState();
                Backgammon backgammon = new Backgammon();
                backgammon.restore(state);

                ActionType actionType = ActionType.parse(actionDto.getAction());
                if(actionType != null) {
                    switch (actionType) {
                        case Roll:
                            if (backgammon.getState().getStatus() == Status.Rolling || backgammon.getState().getStatus() == Status.Initial) {
                                backgammon.roll(randomDieStrategy.roll(), randomDieStrategy.roll());
                                game.setState(backgammon.encode());
                                game.setNextAction();
                                gameRepository.save(game);
                                return ResponseEntity.ok().location(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();
                            } else {
                                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
                            }

                        case Move:
                            Point source = Point.decode(actionDto.getSource());
                            Point destination = Point.decode(actionDto.getDestination());
                            if (backgammon.getState().getStatus() == Status.Moving && backgammon.getState().getMoves().isMovable(source, destination)) {
                                backgammon.move(source, destination);
                                game.setState(backgammon.encode());
                                game.setNextAction();
                                gameRepository.save(game);
                                return ResponseEntity.ok().location(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();
                            } else {
                                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
                            }

                        case PickUpDice:
                            if (backgammon.getState().getStatus() == Status.NoMoves) {
                                backgammon.clearDice();
                                game.setState(backgammon.encode());
                                game.setNextAction();
                                gameRepository.save(game);
                                return ResponseEntity.ok().location(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();
                            } else {
                                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
                            }

                        case OfferDouble:
                            if (backgammon.getState().getStatus() == Status.Rolling || backgammon.getState().getCubeOwner() == backgammon.getState().getCurrentPlayer()) {
                                backgammon.doubleStake();
                                game.setState(backgammon.encode());
                                game.setNextAction();
                                gameRepository.save(game);
                                return ResponseEntity.ok().location(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();
                            } else {
                                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
                            }

                        case AcceptDouble:
                            if (backgammon.getState().getStatus() == Status.DoubleStake) {
                                backgammon.acceptDouble();
                                game.setState(backgammon.encode());
                                game.setNextAction();
                                gameRepository.save(game);
                                return ResponseEntity.ok().location(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();
                            } else {
                                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
                            }

                        case Quit:
                            if (backgammon.getState().getStatus() == Status.DoubleStake) {
                                backgammon.rejectDouble();
                            } else {
                                //todo quit game
                                //backgammon.
                            }
                            game.setState(backgammon.encode());
                            game.setNextAction();
                            gameRepository.save(game);
                            return ResponseEntity.ok().location(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();

                        default:
                            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
                    }
                } else {
                    return ResponseEntity.badRequest().body(new ApiError(
                            HttpStatus.BAD_REQUEST,
                            "Invalid Message Body", "Invalid action: " + actionDto.getAction()));
                }
            } else {
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleConflict(Exception e, HttpServletResponse response)
            throws IOException {

        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    */
}