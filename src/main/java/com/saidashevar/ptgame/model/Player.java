package com.saidashevar.ptgame.model;

import java.util.ArrayList;
import java.util.Collections;

import lombok.Data;

@Data
public class Player {
	private Turn turn = new Turn();;
	private ArrayList<Card> discardPile = new ArrayList<>(10);
	private ArrayList<Card> deck = new ArrayList<>(25);
	private ArrayList<Card> hand = new ArrayList<>(6);
	private Card[][] board = new Card[3][3];
	
	//for some time, while there is no database, cards are added here
	{
		deck.add(new Card("Cursed Knight", 3, 6));
		deck.add(new Card("Diabolist", 1, 3));
		deck.add(new Card("Divinity", 2, 4));
		deck.add(new Card("Druid", 2, 5));
		deck.add(new Card("Chronicler", 1, 4));
		deck.add(new Card("Immortal", 3, 2));
		deck.add(new Card("Inventor", 1, 5));
		deck.add(new Card("Lorekeeper", 2, 4));
		deck.add(new Card("Magical Knight", 2, 4));
		deck.add(new Card("Puppeter", 2, 4));
		deck.add(new Card("Relic Hunter", 3, 7));
		deck.add(new Card("Sage", 0, 4));
		deck.add(new Card("Plague Bearer", 3, 4));
		deck.add(new Card("Monster Hunter", 3, 5));
		deck.add(new Card("Mastermind", 3, 6));
		deck.add(new Card("Planebinder", 2, 5));
		deck.add(new Card("Operative", 2, 5));
		deck.add(new Card("Necromancer", 2, 6));
		deck.add(new Card("Warmage", 4, 3));
		deck.add(new Card("Tactitian", 0, 4));
		deck.add(new Card("Supervillain", 0, 3));
		deck.add(new Card("Warlock", 2, 3));
		deck.add(new Card("Technologist", 3, 3));
		deck.add(new Card("Sniper", 6, 3));
		deck.add(new Card("Werewolf", 3, 5));
		Collections.shuffle(deck);
	}
}
