package com.dkelava.backgammon.websrv.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.dkelava.backgammon.bglib.game.actions.actions.*;
import com.dkelava.backgammon.bglib.model.*;
import com.dkelava.backgammon.websrv.domain.*;
import com.dkelava.backgammon.websrv.exceptions.*;
import com.dkelava.backgammon.websrv.resources.*;
import com.dkelava.backgammon.websrv.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private GameRequestService gameRequestService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // REQUEST GAME
    @RequestMapping(path = "/requests", method = RequestMethod.POST)
    public ResponseEntity<?> requestGame(@RequestBody GameRequestDto gameRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        GameRequest gameRequest = gameRequestService.createGameRequest(username, gameRequestDto.getOpponent());
        URI gameRequestLink = linkTo(methodOn(GameController.class).getGameRequest(gameRequest.getId())).toUri();
        if(username.equals(gameRequestDto.getOpponent())) {
            Game game = gameService.createGame(gameRequest.getChallenger().getName(), gameRequest.getOpponent().getName(), new Backgammon().encode());
            gameRequest.setGame(game);
            gameRequestService.save(gameRequest);
        } else {
            simpMessagingTemplate.convertAndSendToUser(gameRequestDto.getOpponent(), "/offer", gameRequestLink.toString());
        }
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(gameRequestLink).build();
    }

    // GET GAME REQUEST
    @RequestMapping(path = "/requests/{gameRequestId}", method = RequestMethod.GET)
    public ResponseEntity<?> getGameRequest(@PathVariable int gameRequestId) {
        GameRequest gameRequest = gameRequestService.getGameRequest(gameRequestId);
        GameRequestResource gameRequestResource = new GameRequestResource(gameRequest.getChallenger().getName(), gameRequest.getOpponent().getName(), gameRequest.isAccepted());
        gameRequestResource.add(linkTo(methodOn(GameController.class).getGameRequest(gameRequestId)).withSelfRel());
        gameRequestResource.add(linkTo(methodOn(PlayerController.class).getPlayer(gameRequest.getChallenger().getName())).withRel("challenger"));
        gameRequestResource.add(linkTo(methodOn(PlayerController.class).getPlayer(gameRequest.getOpponent().getName())).withRel("opponent"));
        if(gameRequest.isAccepted()) {
            gameRequestResource.add(linkTo(methodOn(GameController.class).getGame(gameRequest.getGame().getId())).withRel("game"));
        }
        return ResponseEntity.ok(gameRequestResource);
    }

    // ACCEPT GAME REQUEST
    @RequestMapping(path = "/requests/{gameRequestId}", method = RequestMethod.POST)
    public ResponseEntity<?> acceptGameRequest(@PathVariable int gameRequestId) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        GameRequest gameRequest = gameRequestService.getGameRequest(gameRequestId);
        if(!username.equals(gameRequest.getOpponent().getName())) {
            throw new AccessDeniedException("Only challenged opponent can accept game");
        }
        if(gameRequest.isAccepted()) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER).location(linkTo(methodOn(GameController.class).getGame(gameRequest.getGame().getId())).toUri()).build();
        }
        Game game = gameService.createGame(gameRequest.getChallenger().getName(), gameRequest.getOpponent().getName(), new Backgammon().encode());
        gameRequest.setGame(game);
        gameRequestService.save(gameRequest);
        URI gameLink = linkTo(methodOn(GameController.class).getGame(game.getId())).toUri();
        simpMessagingTemplate.convertAndSendToUser(gameRequest.getChallenger().getName(), "/acceptGame", gameLink.toString());
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(gameLink).build();
    }

    // GET GAME STATE
    @RequestMapping(path = "/games/{gameId}", method = RequestMethod.GET)
    public ResponseEntity<GameResource> getGame(@PathVariable int gameId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Game game = gameService.getGame(gameId);
        Backgammon backgammon = new Backgammon();
        try {
            backgammon.restore(game.getState());
        } catch(Exception ex) {
            throw new ServerError("Invalid backgammon state");
        }
        GameResource gameResource = new GameResource(backgammon.getState());
        gameResource.add(linkTo(methodOn(GameController.class).getGame(gameId)).withSelfRel());
        gameResource.add(linkTo(methodOn(PlayerController.class).getPlayer(game.getWhitePlayer().getName())).withRel("whitePlayer"));
        gameResource.add(linkTo(methodOn(PlayerController.class).getPlayer(game.getBlackPlayer().getName())).withRel("blackPlayer"));
        if(username.equals(getPlayerName(backgammon.getState().getCurrentPlayer(), game))) {
            gameResource.add(linkTo(methodOn(GameController.class).doAction(gameId, null)).withRel("action"));
        }
        return ResponseEntity.ok(gameResource);
    }

    // DO ACTION
    @RequestMapping(path = "/games/{gameId}", method = RequestMethod.POST)
    public ResponseEntity<?> doAction(@PathVariable(value = "gameId") int gameId, @RequestBody ActionDto actionDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Game game = gameService.getGame(gameId);
        Backgammon backgammon = new Backgammon();
        try {
            backgammon.restore(game.getState());
        } catch (Exception ex) {
            throw new ServerError("Invalid backgammon state");
        }
        if(backgammon.getState().getCurrentPlayer() == Color.White && !game.getWhitePlayer().getName().equals(username)) {
            throw new ForbiddenActionException("Not players turn");
        } else if(backgammon.getState().getCurrentPlayer() == Color.Black && !game.getBlackPlayer().getName().equals(username)) {
            throw new ForbiddenActionException("Not players turn");
        } else if(backgammon.getState().getCurrentPlayer() == Color.None) {
            throw new ForbiddenActionException("Not players turn");
        }
        try {
            Color opponent = backgammon.getState().getCurrentPlayer().getOpponent();
            createAction(actionDto).execute(backgammon, null);
            game.setState(backgammon.encode());
            gameService.saveGame(game);
            URI gameLink = linkTo(methodOn(GameController.class).getGame(game.getId())).toUri();
            simpMessagingTemplate.convertAndSendToUser(getPlayerName(opponent, game), "/action", gameLink.toString());
            return ResponseEntity.status(HttpStatus.SEE_OTHER).location(gameLink).build();
        } catch(Exception ex) {
            throw new InvalidActionException(ex.getMessage());
        }
    }

    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity handleInvalidActionException(InvalidActionException e, HttpServletResponse response) {
        return ResponseEntity.badRequest().body(
                new ApiError(HttpStatus.BAD_REQUEST, "Invalid GameAction", e.getMessage()));
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity handleForbiddenActionException(ForbiddenActionException e, HttpServletResponse response) {
        return ResponseEntity.badRequest().body(
                new ApiError(HttpStatus.FORBIDDEN, "Forbidden GameAction", e.getMessage()));
    }

    @ExceptionHandler(MissingResourceException.class)
    public ResponseEntity handleMissingResourceException(MissingResourceException e, HttpServletResponse response) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ServerError.class)
    public ResponseEntity handleInternalError(ServerError e, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Server error", e.getMessage())
        );
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity handleUnprocessableEntityException(UnprocessableEntityException e, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getMessage())
        );
    }

    private static Action createAction(ActionDto actionDto) {
        ActionType actionType = ActionType.parse(actionDto.getAction());
        PointId source = PointId.from(actionDto.getSource());
        PointId destination = PointId.from(actionDto.getDestination());
        switch (actionType) {
            case Roll:
                return new RollAction();
            case Move:
                return new MoveAction(source.getPoint(), destination.getPoint());
            case PickUpDice:
                return new ClearDiceAction();
            case OfferDouble:
                return new DoubleStakeAction();
            case AcceptDouble:
                return new AcceptDoubleAction();
            case RejectDouble:
                return new RejectDoubleAction();
            // TODO: case Quit:
            default:
                throw new InvalidActionException("Invalid action:" + actionDto.getAction());
        }
    }

    private static String getPlayerName(Color color, Game game) {
        switch (color) {
            case White:
                return game.getWhitePlayer().getName();
            case Black:
                return game.getBlackPlayer().getName();
            default:
                return "";
        }
    }
}
