package com.saidashevar.ptgame.controller;

//eclipse don't want to import static automatically so i just save this here
//nothing bad happens, right?
//import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS;
//import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS_1LEADER_CHOSEN;
//import static com.saidashevar.ptgame.model.GameStatus.FINISHED;
//import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER;
//import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER_1LEADER_CHOSEN;
//import static com.saidashevar.ptgame.model.GameStatus.PEACE;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.controller.request.HireHeroRequest;
import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.service.GameService;
import com.saidashevar.ptgame.service.HeroService;
import com.saidashevar.ptgame.service.PlayerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/heroes")
public class HeroController {
	
	private final HeroService heroService;
	private final PlayerService playerService;
	private final GameService gameService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	HeroRepository heroRepository;
	@Autowired
	CardRepository cardRepository;
	
	@GetMapping
	List<Hero> getHeroes() { return heroRepository.findAll(); }
	
	@PostMapping
	Hero createHero(@RequestBody Hero hero) { return heroRepository.save(hero); }
	
	@GetMapping("/get-heroes")
	ResponseEntity< List<Hero> > getBoard(@RequestParam("id") String gameId, 
										 @RequestParam("login") String login) {
		return ResponseEntity.ok(heroRepository.findAll().stream().filter(card -> card.getPlayer().getLogin().equals(login)).toList());
	}
	
	@PostMapping("/hire-hero") //Returns to player his new hand. And sends board and card count to both players by socket
	public ResponseEntity< Set<Card> > hireHero(@RequestBody HireHeroRequest request) throws InvalidGameException, NotFoundException {
		log.info(request.getLogin() +" hires new Hero!");
		heroService.hireHero(request);
		//Now we have to send both players board, second player must know hero his opponent hired and where.
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
											 gameService.getBoard(request.getGameId()));
		//Second one sends info about card count
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
				 							 gameService.getCardCount(request.getGameId()));
		return ResponseEntity.ok(playerService.getPlayer(request.getLogin()).getHand());
	}
	
	@PostMapping("/hire-leader") //returns hand after choosing leader and sends message that both players have chosen leader
	public ResponseEntity< Set<Card> > hireLeader(@RequestBody HireHeroRequest request) throws NotFoundException, InvalidGameException {
		log.info(request.getLogin() +" hires new Leader!");
		
		Game game = gameService.loadGameService(request.getGameId());
		game.nextLeaderStatus();
		
		Player player = 
		playerService.savePlayer(
			heroService.hireLeader(
				playerService.getPlayer(request.getLogin()),
				request.getCardId()
			)
		);
		
		//Now send players new info about leaders
		simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),
												 gameService.getGame(game));
		
		//As response - new player's hand
		return ResponseEntity.ok(player.getHand());
	}
	
}
