package magirator.objects;

public class Result {
	
	private Deck deck;
	private Play play;
	private Player player;

	public Result(Deck deck, Play play, Player player) {
		super();
		this.deck = deck;
		this.play = play;
		this.player = player;
	}
	
	public Deck getDeck() {
		return deck;
	}
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	public Play getPlay() {
		return play;
	}
	public void setPlay(Play play) {
		this.play = play;
	}
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
