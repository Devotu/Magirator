package magirator.data.collections;

import magirator.data.entities.Deck;
import magirator.data.interfaces.Player;

public class PlayerDeck {
	
	private Player player;
	private Deck deck;

	public PlayerDeck(Player player, Deck deck) {
		this.player = player;
		this.deck = deck;
	}

	public Player getPlayer() {
		return player;
	}

	public Deck getDeck() {
		return deck;
	}
}
