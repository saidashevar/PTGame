package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.controller.request.StringRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.Turn;
import com.saidashevar.ptgame.repository.PlayerRepository;
import com.saidashevar.ptgame.service.GameService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/players")
public class PlayerController {

	private final GameService gameService;
	
	@Autowired
	PlayerRepository playerRepository;
	
	@GetMapping
	List<Player> getPlayers() { return playerRepository.findAll(); }
	
	@PostMapping
	Player createPlayer(@RequestBody Player player) {
		return playerRepository.save(player); }
	
	// Game Management functions
	
	//Method returns player object, who will play game. It checks player with current login, returns it, if it exists, or creates new one. 
	@PostMapping("/checkLogin")
	ResponseEntity<Player> checkLogin(@RequestBody StringRequest request) {
		String login = request.getString();
		log.info(login + " checks his existance in database");
		//should add it all in game service
		try {
			Player player = playerRepository.findById(login)
					.orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found"));
			log.info("Player with login: " + login + " was found");
			return ResponseEntity.ok(player);
//			return ResponseEntity.ok(playerRepository.findAll().stream().filter(p -> p.getLogin().equals(login))
//					.findAny().orElseThrow(() -> new NotFoundException("Player with login: " + login + " wasn't found")));
		} catch (NotFoundException e) {
			log.info("There is no player with login: " + login + ". Creating new one");
			return ResponseEntity.ok(playerRepository.save(new Player(login)));
		}
	}
	
	//Gameplay functions
	
	@GetMapping("/get-hand")
	ResponseEntity< List<Card> > getHand(@RequestParam("id") String gameId, @RequestParam("login") String login) throws NotFoundException, InvalidGameException {
		Game game = gameService.loadGameService(gameId);
		return ResponseEntity.ok(game.findPlayers(login)[0].getHand());
	}
	
	@GetMapping("/get-turn")
	ResponseEntity<Turn> getTurn(@RequestParam("id") String gameId, @RequestParam("login") String login) throws InvalidGameException, NotFoundException {
		Game game = gameService.loadGameService(gameId);
		return ResponseEntity.ok(game.findPlayers(login)[0].getTurn());
	}
}
