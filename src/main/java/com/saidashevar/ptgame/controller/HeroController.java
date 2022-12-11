package com.saidashevar.ptgame.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saidashevar.ptgame.model.Game;
import com.saidashevar.ptgame.model.Hero;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.repository.HeroRepository;
import com.saidashevar.ptgame.service.GameService;
import com.saidashevar.ptgame.service.HeroService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/heroes")
public class HeroController {
	
//	private final HeroService heroService;
//	private final GameService gameService;
	
	@Autowired
	HeroRepository heroRepository;
	
	@GetMapping
	List<Hero> getHeroes() { return heroRepository.findAll(); }
	
	@PostMapping
	Hero createHero(@RequestBody Hero hero) { return heroRepository.save(hero); }
	
	@GetMapping("/get-heroes")
	ResponseEntity< List<Hero> > getBoard(@RequestParam("id") String gameId, 
										 @RequestParam("login") String login) {
		return ResponseEntity.ok(heroRepository.findAll().stream().filter(card -> card.getPlayer().getLogin().equals(login)).toList());
	}
}
