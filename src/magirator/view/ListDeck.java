package magirator.view;

import java.util.Date;

import magirator.data.entities.Deck;

/**
 * A Deck containing all info needed for filering in a list
 * @author ottu
 *
 */
public class ListDeck {
	
	private int id;
	private String name;
	private String format;
	private long blackCards;
	private long whiteCards;
	private long redCards;
	private long greenCards;
	private long blueCards;
	private long colorlessCards;
	private String theme;
	private boolean active;
	private Date created;
	private int winrate;
	private int games;
	
	public ListDeck(Deck deck, int winrate, int games){
		this.id = deck.getDeckid();
		this.name = deck.getName();
		this.format = deck.getFormat();
		this.blackCards = deck.getBlackCards();
		this.whiteCards = deck.getWhiteCards();
		this.redCards = deck.getRedCards();
		this.greenCards = deck.getGreenCards();
		this.blueCards = deck.getBlueCards();
		this.colorlessCards = deck.getColorlessCards();
		this.theme = deck.getTheme();
		this.active = deck.getActive();
		this.created = deck.getDateCreated();
		this.winrate = winrate;
		this.games = games;
	}
	
}
