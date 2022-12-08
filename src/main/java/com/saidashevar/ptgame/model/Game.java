package com.saidashevar.ptgame.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Game {
	
	//private GameStatus status;
	@Id
	private String gameId;
	//private HashMap<String, Player> players = new HashMap<>(2);
	//private String[] logins = new String[2]; //I need to have access to logins somehow. Need better collections understanding.
	private int wave = 0;
	//There must be better solution than keeping hashmap with logins and array with them, but i don't see it now
	
	public Game() {}
}