package com.dkelava.backgammon.websrv.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.dkelava.backgammon.bglib.game.actions.actions.*;
import com.dkelava.backgammon.bglib.model.Backgammon;
import com.dkelava.backgammon.bglib.model.BackgammonState;
import com.dkelava.backgammon.bglib.model.Color;
import com.dkelava.backgammon.websrv.domain.Game;
import com.dkelava.backgammon.websrv.exceptions.InternalError;
import com.dkelava.backgammon.websrv.resources.*;
import com.dkelava.backgammon.websrv.exceptions.ForbiddenActionException;
import com.dkelava.backgammon.websrv.exceptions.InvalidActionException;
import com.dkelava.backgammon.websrv.exceptions.MissingResourceException;
import com.dkelava.backgammon.websrv.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    // CREATE GAME
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<?> createGame(@RequestBody GameDescriptorDto gameDescriptorDto) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Backgammon backgammon = new Backgammon();
        Game game = gameService.createGame(username, gameDescriptorDto.getWhitePlayerName(), gameDescriptorDto.getBlackPlayerName(), backgammon.encode());
        return ResponseEntity.created(linkTo(methodOn(GameController.class).getGame(game.getId())).toUri()).build();
    }

    // ACCEPT GAME
    @RequestMapping(path = "/games/{gameId}", method = RequestMethod.POST)
    public ResponseEntity<?> acceptGame(@PathVariable int gameId) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        gameService.acceptGame(gameId, username);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(linkTo(methodOn(GameController.class).getGame(gameId)).toUri()).build();
    }

    // GET GAME STATE
    @RequestMapping(path = "/games/{gameId}", method = RequestMethod.GET)
    public ResponseEntity<GameResource> getGame(@PathVariable int gameId) throws Exception {
        Game game = gameService.getGame(gameId);
        int actionId = game.getLastAction();
        Backgammon backgammon = new Backgammon();
        try {
            backgammon.restore(game.getState());
        } catch(Exception ex) {
            throw new InternalError("Invalid backgammon state");
        }
        GameResource gameResource = new GameResource(backgammon.getState());
        gameResource.add(linkTo(methodOn(GameController.class).getGame(gameId)).withSelfRel());
        if(!isGameFinished(backgammon.getState())) {
            gameResource.add(linkTo(methodOn(GameController.class).doAction(gameId, actionId + 1, null)).withRel("nextAction"));
        }
        gameResource.add(linkTo(methodOn(PlayerController.class).getPlayer(game.getWhitePlayer().getName())).withRel("whitePlayer"));
        gameResource.add(linkTo(methodOn(PlayerController.class).getPlayer(game.getBlackPlayer().getName())).withRel("blackPlayer"));
        return ResponseEntity.ok(gameResource);
    }

    // ACTION
    @RequestMapping(path = "/games/{gameId}/action/{actionId}", method = RequestMethod.PUT)
    public ResponseEntity<?> doAction(@PathVariable(value = "gameId") int gameId,
                                  @PathVariable(value = "actionId") int actionId,
                                  @RequestBody ActionDto actionDto) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Game game = gameService.getGame(gameId);
        if(actionId != game.getLastAction() + 1) {
            throw new ForbiddenActionException("Changing actions is not allowed");
        } else if(!game.isAcepted()) {
            throw new ForbiddenActionException("Game is not accepted");
        } else {
            Backgammon backgammon = new Backgammon();
            try {
                backgammon.restore(game.getState());
            } catch (Exception ex) {
                throw new InternalError("Invalid backgammon state");
            }
            if(backgammon.getState().getCurrentPlayer() == Color.White && !game.getWhitePlayer().getName().equals(username)) {
                throw new ForbiddenActionException("Not players turn");
            } else if(backgammon.getState().getCurrentPlayer() == Color.Black && !game.getBlackPlayer().getName().equals(username)) {
                throw new ForbiddenActionException("Not players turn");
            } else if(backgammon.getState().getCurrentPlayer() == Color.None) {
                throw new ForbiddenActionException("Not players turn");
            }
            try {
                createAction(actionDto).execute(backgammon, null);
                game.setState(backgammon.encode());
                game.setNextAction();
                gameService.saveGame(game);
                return ResponseEntity.created(linkTo(methodOn(GameController.class).getAction(gameId, actionId)).toUri()).build();
            } catch(Exception ex) {
                throw new InvalidActionException(ex.getMessage());
            }
        }
    }

    // GET ACTION
    @RequestMapping(path = "/games/{gameId}/action/{actionId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAction(@PathVariable(value = "gameId") int gameId,
                                      @PathVariable(value = "actionId") int actionId) throws Exception {
        //gameService.getGame(gameId);
        // TODO: return body with action data
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity handleInvalidActionException(Exception e, HttpServletResponse response) throws IOException {
        return ResponseEntity.badRequest().body(
                new ApiError(HttpStatus.BAD_REQUEST, "Invalid GameAction", e.getMessage()));
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity handleForbiddenActionException(Exception e, HttpServletResponse response) throws IOException {
        return ResponseEntity.badRequest().body(
                new ApiError(HttpStatus.FORBIDDEN, "Forbidden GameAction", e.getMessage()));
    }

    @ExceptionHandler(MissingResourceException.class)
    public ResponseEntity handleMissingResourceException(Exception e, HttpServletResponse response) throws IOException {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InternalError.class)
    public ResponseEntity handleInternalError(Exception e, HttpServletResponse response) throws IOException {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Server error", e.getMessage())
        );
    }

    private static boolean isGameFinished(BackgammonState backgammonState) {
        switch (backgammonState.getStatus()) {
            case Initial:
            case Rolling:
            case Moving:
            case DoubleStake:
            case NoMoves:
                return false;
            default:
                return true;
        }
    }

    private Action createAction(ActionDto actionDto) {
        ActionType actionType = ActionType.parse(actionDto.getAction());
        PointId source = PointId.parse(actionDto.getSource());
        PointId destination = PointId.parse(actionDto.getDestination());
        switch (actionType) {
            case Roll:
                return new RollAction();
            case Move:
                return new MoveAction(source.create(), destination.create());
            case PickUpDice:
                return new ClearDiceAction();
            case OfferDouble:
                return new DoubleStakeAction();
            case AcceptDouble:
                return new AcceptDoubleAction();
            // TODO: case Quit:
            default:
                throw new InvalidActionException("Invalid action:" + actionDto.getAction());
        }
    }
}
