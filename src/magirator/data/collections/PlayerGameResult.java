package magirator.data.collections;

import java.util.ArrayList;

import magirator.data.entities.Deck;
import magirator.data.entities.Game;
import magirator.data.entities.Rating;
import magirator.data.entities.Result;
import magirator.data.entities.Tag;
import magirator.data.entities.Player;

/**
 * A PlayerGameResult is a result for a player who has played a game
 * The collection consist of a Player, (a Use), a Deck, a Play and a Game
 * where the player played the deck in the game 
 * Tags and rating are are optional
 * @author ottu
 *
 */
public class PlayerGameResult {

	//Core
	private Player player;
	private Deck deck;
	private Result result;
	private Game game;
	
	//Optional
	private Rating rating;
	private ArrayList<Tag> tags;

	public PlayerGameResult(Player player, Deck deck, Result result, Game game) {
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
	
	public Rating getRating() {
		return rating;
	}
	
	public void setRating(Rating rating){
		this.rating = rating;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
}
