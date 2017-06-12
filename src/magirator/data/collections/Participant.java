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
	private boolean minion;
	private boolean confirmed;
	private int place;

	public Participant(IPlayer player, Deck deck, int life, boolean isMinion, boolean isConfirmed, int place) {
		this.player = player;
		this.deck = deck;
		this.life = life;
		this.minion = isMinion;
		this.confirmed = isConfirmed;
		this.place = place;
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

	public boolean isMinion() {
		return minion;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

	public int getPlace() {
		return place;
	}
}
