package com.saidashevar.ptgame.model.cards;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saidashevar.ptgame.model.Player;
import com.saidashevar.ptgame.model.effects.EffectBasic;
import com.saidashevar.ptgame.model.effects.EffectSimple;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "players_heroes")
public class Hero extends CardBasis {
	
	@Column(name = "coord_x", nullable = true) //Caution! Here is matrix system is used. x means row, y - column.
	private int coordX;
	
	@Column(name = "coord_y", nullable = true)
	private int coordY;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "heroes_effects",
			joinColumns = @JoinColumn(name = "hero_id"),
			inverseJoinColumns = @JoinColumn(name = "effect_id"))
	private Set<EffectBasic> effects = new HashSet<>();
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //You don't even imagine how much time i've spent to find this line of code (15 minutes)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "player_login", referencedColumnName = "login")
	private Player player;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "cardId", referencedColumnName = "id") //id - it is card id.
	private Card card;
	
	@Override
	public int getAttack() {
		return super.getAttack(); //this for time being
	}
	
	@Override
	public EffectSimple takeDamage(int attackValue) {
		//Next code searches for current damage and adjusts new if present
		//using set for effects is... inefficient, but it is hard to use map with hibernate
		Iterator<EffectBasic> itr = effects.iterator();
		while ( itr.hasNext() ) {
			EffectBasic effect = itr.next(); 
			if(effect.getName().equals("damaged")) {
				effect.setValue(effect.getValue() + attackValue);
				return (EffectSimple)effect;
			}
		} 
		//If hero has no damage, next line will add it
		var damageEffect = new EffectSimple("damaged", attackValue);
		return damageEffect;
	}
	
	public int getEffectValue(String effectName) {
		Iterator<EffectBasic> itr = effects.iterator();
		while ( itr.hasNext() ) {
			EffectBasic effect = itr.next(); 
			if(effect.getName().equals(effectName)) {
				return effect.getValue();
			}
		}
		return 0;
	}
	
	public void saveEffect(EffectBasic effect) {
		effects.add(effect);
	}
	
	public Hero removeEffects() {
		effects.clear();
		return this;
	}
	
	//Next are constructors. Almost all of them are used somewhere. 
	public Hero() {}
	
	public Hero(int edition, String name, int attack, int maxHealth) {
		super(edition, name, attack, maxHealth);
	}
	
	public Hero(Card card) {
		super(
			card.getEdition(),
			card.getName(),
			card.getAttack(),
			card.getMaxHealth());
		this.card = card;
	}
	
	public Hero(int coordX, int coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	public Hero (Card card, int coordX, int coordY) {
		this(card);
		this.coordX = coordX;
		this.coordY = coordY;
	}
	
	public Hero(Card card ,int coordX, int coordY, Player player) {
		this(card, coordX, coordY);
		this.player = player;
	}
	
	public Hero(int coordX, int coordY, Player player) {
		this(coordX, coordY);
		this.player = player;
	}
	
	public Hero(Card card, Hero heroWithoutCard) {
		this(card);
		this.coordX = heroWithoutCard.getCoordX();
		this.coordY = heroWithoutCard.getCoordY();
		this.player = heroWithoutCard.getPlayer();
	}
	
	//Getters and setters
	public Set<EffectBasic> getEffects() {
		return effects;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

	public int getCoordX() {
		return coordX;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}
	
	public Card getCard() {
		return card;
	}
}