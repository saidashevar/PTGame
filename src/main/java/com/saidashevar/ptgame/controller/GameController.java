package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saidashevar.ptgame.controller.request.StringRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.service.GameService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/games")
@AllArgsConstructor
@Slf4j
public class GameController {

	private final GameService gameService;
//	private final SimpMessagingTemplate simpMessagingTemplate;

	// Rest methods
	@GetMapping
	List<Game> getGames() {
		return gameService.getGameRepository().findAll();
	}

	@PostMapping
	Game createGame(@RequestBody Game game) {
		return gameService.getGameRepository().save(game);
	}
	
	@GetMapping("/get-game")
	ResponseEntity<Game> getGame(@RequestParam("id") String gameId) throws NotFoundException, InvalidGameException {
		log.info("Requested game with ID: " + gameId);
		return ResponseEntity.ok(gameService.loadGameService(gameId));
	}
	
	//
	// Game managing methods, maybe i should unite them with rest methods
	//
	
	@PostMapping("/start")
	public ResponseEntity<Game> startGame(@RequestBody Player player) throws NotFoundException {
		log.info("start game request: {}", player.getLogin());
		return ResponseEntity.ok(gameService.createGame(player.getLogin()));
	}

	@PostMapping("/connect/random")
	public ResponseEntity<Game> connectRandom(@RequestBody Player player) throws NotFoundException { 
		log.info("connect random from {}", player.getLogin());
		return ResponseEntity.ok(gameService.connectToRandomGame(player)); 
	}
}
