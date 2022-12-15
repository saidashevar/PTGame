package com.saidashevar.ptgame.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.model.Card;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;

//@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cards")
public class CardController {
	
//	private final CardService cardService;
//	private final PlayerService playerService;
//	private final GameService gameService;
//	private final SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	CardRepository cardRepository;
	
	@Autowired
	PlayerRepository playerRepository;
	
	@GetMapping
	List<Card> getCards() {	return cardRepository.findAll(); }
	
	@PostMapping
	Card createCard(@RequestBody Card card) {
		return cardRepository.save(card);
	}
	
	@PutMapping("/{cardId}/players/{playerLogin}")
	Card connectPlayerWithCard(
			@PathVariable Long cardId,
			@PathVariable String playerLogin
			) throws NotFoundException {
		Card card = cardRepository.findById(cardId)
				.orElseThrow(() -> new NotFoundException("card named " + cardId + " wasn't found"));
		Player player = playerRepository.findById(playerLogin)
				.orElseThrow(() -> new NotFoundException("player with login: " + playerLogin + "wasn't found"));
		card.addInDeck(player);
		return cardRepository.save(card);
	}	
	
	@GetMapping("/updateAll") //fast restoring
	void createCards() {
		cardRepository.save(new Card(2, "Cursed_Knight", 3, 6));
		cardRepository.save(new Card(2, "Diabolist", 1, 3));
		cardRepository.save(new Card(2, "Divinity", 2, 4));
		cardRepository.save(new Card(2, "Druid", 2, 5));
		cardRepository.save(new Card(2, "Chronicler", 1, 4));
		cardRepository.save(new Card(2, "Immortal", 3, 2));
		cardRepository.save(new Card(2, "Inventor", 1, 5));
		cardRepository.save(new Card(2, "Lorekeeper", 2, 4));
		cardRepository.save(new Card(2, "Magical_Knight", 2, 4));
		cardRepository.save(new Card(2, "Puppeter", 2, 4));
		cardRepository.save(new Card(2, "Relic_Hunter", 3, 7));
		cardRepository.save(new Card(2, "Sage", 0, 4));
		cardRepository.save(new Card(2, "Plague_Bearer", 3, 4));
		cardRepository.save(new Card(2, "Monster_Hunter", 3, 5));
		cardRepository.save(new Card(2, "Mastermind", 3, 6));
		cardRepository.save(new Card(2, "Planebinder", 2, 5));
		cardRepository.save(new Card(2, "Operative", 2, 5));
		cardRepository.save(new Card(2, "Necromancer", 2, 6));
		cardRepository.save(new Card(2, "Warmage", 4, 3));
		cardRepository.save(new Card(2, "Tactician", 0, 4));
		cardRepository.save(new Card(2, "Supervillain", 0, 3));
		cardRepository.save(new Card(2, "Warlock", 2, 3));
		cardRepository.save(new Card(2, "Technologist", 3, 3));
		cardRepository.save(new Card(2, "Sniper", 6, 3));
		cardRepository.save(new Card(2, "Werewolf", 3, 5));
	}
}
