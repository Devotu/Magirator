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
	private Play play;
	private Game game;
	
	public Participant(Player player, Deck deck, Play play, Game game) {
		super();
		this.player = player;
		this.deck = deck;
		this.play = play;
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public Deck getDeck() {
		return deck;
	}

	public Play getPlay() {
		return play;
	}

	public Game getGame() {
		return game;
	}
}
