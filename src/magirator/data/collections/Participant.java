package magirator.data.collections;

import java.util.ArrayList;

import magirator.data.interfaces.IPlayer;
import magirator.data.objects.Deck;
import magirator.data.objects.Game;
import magirator.data.objects.Rating;
import magirator.data.objects.Result;
import magirator.data.objects.Tag;

/**
 * A Participant is a collection of a Player, (a Use), a Deck, a Play and a Game
 * where the player played the deck in the game 
 * Tags and rating are are optional
 * @author ottu
 *
 */
public class Participant {

	//Core
	private IPlayer player;
	private Deck deck;
	private Result result;
	private Game game;
	
	//Optional
	private Rating rating;
	private ArrayList<Tag> tags;

	public Participant(IPlayer player, Deck deck, Result result, Game game) {
		this.player = player;
		this.deck = deck;
		this.result = result;
		this.game = game;
		this.tags = new ArrayList<Tag>();
	}

	public IPlayer getPlayer() {
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
