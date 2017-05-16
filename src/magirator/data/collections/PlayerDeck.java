package magirator.data.collections;

import magirator.data.entities.Deck;
import magirator.data.interfaces.IPlayer;

public class PlayerDeck {
	
	private IPlayer player;
	private Deck deck;

	public PlayerDeck(IPlayer player, Deck deck) {
		this.player = player;
		this.deck = deck;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public Deck getDeck() {
		return deck;
	}
}
