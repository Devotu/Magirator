package magirator.dataobjects;

import java.util.ArrayList;

/**
 * A Participant is a collection of a Player, (a Use), a Deck, a Play and a Game
 * where the player played the deck in the game 
 * Tags are an appendix and are optional
 * @author ottu
 *
 */
public class Participant {

	private Player player;
	private Deck deck;
	private Result result;
	private Game game;
	private ArrayList<Tag> tags;

	public Participant(Player player, Deck deck, Result result, Game game) {
		this.player = player;
		this.deck = deck;
		this.result = result;
		this.game = game;
		this.tags = new ArrayList<Tag>();
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
	
	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
}