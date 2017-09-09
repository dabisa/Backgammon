package com.dkelava.backgammon.websrv.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.dkelava.backgammon.bglib.game.*;
import com.dkelava.backgammon.bglib.game.actions.actions.*;
import com.dkelava.backgammon.bglib.model.*;
import com.dkelava.backgammon.bglib.nn.BackgammonNeuralNetwork;
import com.dkelava.backgammon.websrv.domain.*;
import com.dkelava.backgammon.websrv.domain.Game;
import com.dkelava.backgammon.websrv.domain.Player;
import com.dkelava.backgammon.websrv.exceptions.*;
import com.dkelava.backgammon.websrv.resources.*;
import com.dkelava.backgammon.websrv.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private PlayerService playerService;
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
        Player opponent = playerService.getPlayer(gameRequestDto.getOpponent());
        if((opponent != null && opponent.getPlayerType() == PlayerType.AI) || username.equals(gameRequestDto.getOpponent())) {
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
        boolean isForActivePlayer = username.equals(getPlayerName(backgammon.getState().getCurrentPlayer(), game));
        GameResource gameResource = getGameResource(game, backgammon, isForActivePlayer);
        return ResponseEntity.ok(gameResource);
    }

    private GameResource getGameResource(Game game, Backgammon backgammon, boolean isForActivePlayer) {
        GameResource gameResource = new GameResource(backgammon.getState());
        gameResource.add(linkTo(methodOn(GameController.class).getGame(game.getId())).withSelfRel());
        gameResource.add(linkTo(methodOn(PlayerController.class).getPlayer(game.getWhitePlayer().getName())).withRel("whitePlayer"));
        gameResource.add(linkTo(methodOn(PlayerController.class).getPlayer(game.getBlackPlayer().getName())).withRel("blackPlayer"));
        if(isForActivePlayer) {
            //gameResource.add(linkTo(methodOn(GameController.class).doAction(gameId, null)).withRel("action"));
            try {
            gameResource.add(linkTo(GameController.class, GameController.class.getMethod("doAction", Integer.class, ActionDto.class, HttpServletResponse.class), game.getId()).withRel("action"));
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        return gameResource;
    }

    // DO ACTION
    @RequestMapping(path = "/games/{gameId}", method = RequestMethod.POST)
    public void doAction(@PathVariable(value = "gameId") Integer gameId, @RequestBody ActionDto actionDto, HttpServletResponse response) {
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
            Color opponentColor = backgammon.getState().getCurrentPlayer().getOpponent();
            Color playerColor = backgammon.getState().getCurrentPlayer();
            Boolean[] isMoveHit = new Boolean[1];
            createAction(actionDto).execute(backgammon, new GameObserverBase() {
                @Override
                public void onMove(BackgammonState state, Point source, Point destination, boolean isHit) {
                    isMoveHit[0] = isHit;
                }
            });
            game.setState(backgammon.encode());
            gameService.saveGame(game);
            URI gameLink = linkTo(methodOn(GameController.class).getGame(game.getId())).toUri();
            String playerName = getPlayerName(playerColor, game);
            String opponentName = getPlayerName(opponentColor, game);
            Player opponent = playerService.getPlayer(opponentName);

            // generate response
            if(backgammon.getState().getCurrentPlayer().equals(playerColor)) {
                response.setStatus(HttpServletResponse.SC_SEE_OTHER);
                response.setHeader("Location", gameLink.toString());
                java.io.PrintWriter wr = response.getWriter();
                wr.flush();
                wr.close();
            } else {
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                response.setContentType("application/json");
                GameStateDto gameState = new GameStateDto(backgammon.getState());
                java.io.PrintWriter wr = response.getWriter();
                ObjectMapper objectMapper = new ObjectMapper();
                wr.print(objectMapper.writeValueAsString(gameState));
                wr.flush();
                wr.close();
            }

            // send messages to opponent
            if(opponent != null && opponent.getPlayerType() == PlayerType.AI) {
                BackgammonNeuralNetwork nn = playerService.getNeuralNetwork(opponentName);
                while(backgammon.getState().getCurrentPlayer() == opponentColor) {
                    Action action = nn.getStrategy().play(backgammon.getState());
                    final ActionType[] actionType = new ActionType[1];
                    final Point[] source = new Point[1];
                    final Point[] destination = new Point[1];
                    final Boolean[] isHit = new Boolean[1];
                    action.execute(backgammon, new GameObserverBase() {
                        @Override
                        public void onInitialRoll(BackgammonState state) {
                            actionType[0] = ActionType.Roll;
                        }

                        @Override
                        public void onRoll(BackgammonState state) {
                            actionType[0] = ActionType.Roll;
                        }

                        @Override
                        public void onDiceCleared(BackgammonState state) {
                            actionType[0] = ActionType.PickUpDice;
                        }

                        @Override
                        public void onMove(BackgammonState state, Point s, Point d, boolean hit) {
                            actionType[0] = ActionType.Move;
                            source[0] = s;
                            destination[0] = d;
                            isHit[0] = hit;
                        }

                        @Override
                        public void onDouble(BackgammonState state) {
                            actionType[0] = ActionType.OfferDouble;
                        }

                        @Override
                        public void onDoubleAccepted(BackgammonState state) {
                            actionType[0] = ActionType.AcceptDouble;
                        }

                        @Override
                        public void onSurrender(BackgammonState state) {
                            actionType[0] = ActionType.RejectDouble;
                        }
                    });

                    game.setState(backgammon.encode());
                    gameService.saveGame(game);

                    String actionName = actionType[0].getName();
                    String sourceName = source[0] != null ? PointId.from(source[0]).getName() : null;
                    String destinationName = destination[0] != null ? PointId.from(destination[0]).getName() : null;
                    Boolean hit = isHit[0];
                    GameStateDto gameState = new GameStateDto(backgammon.getState());
                    System.out.println("sending action: "+actionName);
                    boolean isLast = !backgammon.getState().getCurrentPlayer().equals(opponentColor) || backgammon.getState().getStatus().equals(Status.End);
                    simpMessagingTemplate.convertAndSendToUser(playerName, "/action", new ActionEventDto(isLast ? gameLink : null, actionName, sourceName, destinationName, hit, isLast, gameState));
                }
            } else {
                boolean isLast = !backgammon.getState().getCurrentPlayer().equals(playerColor) || backgammon.getState().getStatus().equals(Status.End);
                GameStateDto gameState = new GameStateDto(backgammon.getState());
                simpMessagingTemplate.convertAndSendToUser(opponentName, "/action", new ActionEventDto(isLast ? gameLink : null, actionDto.getAction(), actionDto.getSource(), actionDto.getDestination(), isMoveHit[0], isLast, gameState));
            }
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
