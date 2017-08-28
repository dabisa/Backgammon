package com.dkelava.backgammon.websrv.controller;

import com.dkelava.backgammon.websrv.domain.Player;
import com.dkelava.backgammon.websrv.resources.PlayerResource;
import com.dkelava.backgammon.websrv.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@CrossOrigin
@RestController
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @RequestMapping(path = "/players/{playerName}", method = RequestMethod.GET)
    public ResponseEntity<?> getPlayer(@PathVariable String playerName) {
        Player player = playerService.getPlayer(playerName);
        PlayerResource playerResource = new PlayerResource(
                player.getName(),
                player.getGamesWon(),
                player.getGamesDraw(),
                player.getGamesLost()
        );
        playerResource.add(linkTo(methodOn(PlayerController.class).getPlayer(playerName)).withSelfRel());
        return ResponseEntity.ok(playerResource);
    }
}
