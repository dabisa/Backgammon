package com.dkelava.backgammon.websrv;

import com.dkelava.backgammon.bglib.model.Backgammon;
import com.dkelava.backgammon.websrv.services.GameService;
import com.dkelava.backgammon.websrv.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
		playerService.createPlayer("Floyd");
		playerService.createPlayer("Dabisa");
		Backgammon backgammon = new Backgammon();
		gameService.createGame("Dabisa","Floyd", "Dabisa", backgammon.encode());
	}
}
