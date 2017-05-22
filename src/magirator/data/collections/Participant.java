package magirator.data.collections;

import magirator.data.entities.Deck;
import magirator.data.interfaces.IPlayer;

/**All parameters needed to keep track of a player during a game
 * @author ottu
 *
 */
public class Participant {

	private IPlayer player;
	private Deck deck;
	private int life;

	public Participant(IPlayer player, Deck deck, int life) {
		this.player = player;
		this.deck = deck;
		this.life = life;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public Deck getDeck() {
		return deck;
	}
	
	public int getLife(){
		return life;
	}
}
