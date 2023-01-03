package com.saidashevar.ptgame.service;

import java.util.ArrayList;
import java.util.List;

//import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS;
//import static com.saidashevar.ptgame.model.GameStatus.CHOOSING_LEADERS_1LEADER_CHOSEN;
//import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER;
//import static com.saidashevar.ptgame.model.GameStatus.NO2PLAYER_1LEADER_CHOSEN;
//import static com.saidashevar.ptgame.model.GameStatus.PEACE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saidashevar.ptgame.exception.InvalidGameException;
import com.saidashevar.ptgame.exception.NotFoundException;
import com.saidashevar.ptgame.exception.game.NoMoreActionsLeftException;
import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.cards.Card;
import com.saidashevar.ptgame.model.cards.Hero;
import com.saidashevar.ptgame.model.cards.Leader;
import com.saidashevar.ptgame.model.cards.LeaderBasis;
import com.saidashevar.ptgame.repository.CardRepository;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.repository.LeaderBasisRepository;
import com.saidashevar.ptgame.repository.LeaderRepository;
import com.saidashevar.ptgame.repository.PlayerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class HeroService {
	
	@Autowired
	HeroRepository heroRepository;
	@Autowired
	CardRepository cardRepository;
	@Autowired
	PlayerRepository playerRepository;
	@Autowired
	LeaderRepository leaderRepository;
	@Autowired
	LeaderBasisRepository leaderBasisRepository;
	
	
	//Returns all heroes on board, excluding leaders.
	public List<Hero> getHeroes(Game game) {
		List<Hero> allHeroes = new ArrayList<>();
		game.getPlayers().stream().forEach(
				p -> allHeroes.addAll(
					heroRepository.findHeroesOfPlayer(p)
				)
			);
		return allHeroes;
	}
	
	public boolean hireHero(Game game, Player player, int y, int cardId) throws InvalidGameException, NotFoundException, NoMoreActionsLeftException { //this is not necessary to return anything
		
		if (!heroRepository.heroOnPlace(player.getLogin(), game.getWave(), y)) {
			log.info("No hero is on this place, hero successfully hired");;
			Card card = cardRepository.findById(cardId)
					.orElseThrow(() -> new NotFoundException("Card with id: " + cardId + " wasn't found"));
			player.makeAction();
			player.removeCardFromHand(card);
			
			playerRepository.save(player);
			heroRepository.save(new Hero(card, game.getWave(), y, player));
			
			return true;
		} else
			log.info("On this place some hero was found!");
			return false;
	}
	
	public Player hireLeader(Player player, int leaderId) throws NotFoundException { //this is not necessary to return anything
		LeaderBasis leaderBasis = leaderBasisRepository.findById(leaderId + 25) 									//For now leaders have id of connected cards +25. It may be changed many ways, so this line with !CAUTION!
				.orElseThrow(() -> new NotFoundException("Leader with that id: " + leaderId + " doesn't exist")); 	//I won't create LeaderService for this
		player.setLeader(new Leader(leaderBasis, player));
		player.removeLeaderCardFromHand(leaderId);
		
		return player;
	}
}
