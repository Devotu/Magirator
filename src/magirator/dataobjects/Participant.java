package magirator.dataobjects;

/**
 * A Participant is a collection of a Player, (a Use), a Deck, a Play and a Game
 * where the player played the deck in the game 
 * @author ottu
 *
 */
public class Participant {

	private Player player;
	private Deck deck;
	private Result result;
	private Game game;
	
	public Participant(Player player, Deck deck, Result result, Game game) {
		super();
		this.player = player;
		this.deck = deck;
		this.result = result;
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public Deck getDeck() {
		return deck;
	}

	public Result getResult() {
		return result;
	}

	public Game getGame() {
		return game;
	}
}
