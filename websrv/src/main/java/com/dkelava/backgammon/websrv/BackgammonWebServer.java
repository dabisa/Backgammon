package com.dkelava.backgammon.websrv;

import com.dkelava.backgammon.bglib.model.Backgammon;
import com.dkelava.backgammon.websrv.services.GameService;
import com.dkelava.backgammon.websrv.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class BackgammonWebServer implements CommandLineRunner {

	@Autowired
	private GameService gameService;
	@Autowired
	private PlayerService playerService;

	public static void main(String[] args) {
		SpringApplication.run(BackgammonWebServer.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		playerService.createAIPlayer("Floyd", new File("floyd.dat"));
		playerService.createHumanPlayer("Dabisa");
		gameService.createGame("Floyd", "Dabisa", new Backgammon().encode());
		gameService.createGame("Dabisa", "Dabisa", new Backgammon().encode());
	}
}
