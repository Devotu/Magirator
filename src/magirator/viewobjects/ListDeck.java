package magirator.viewobjects;

import java.util.Date;

import magirator.dataobjects.Deck;

public class ListDeck {
	
	private int deckid;
	private String name;
	private String format;
	private boolean black;
	private boolean white;
	private boolean red;
	private boolean green;
	private boolean blue;
	private boolean colorless;
	private String theme;
	private boolean active;
	private Date created;
	private int winrate;
	private int games;
	
	public ListDeck(Deck deck, int winrate, int games){
		this.deckid = deck.getDeckid();
		this.name = deck.getName();
		this.format = deck.getFormat();
		this.black = deck.getBlack();
		this.white = deck.getWhite();
		this.red = deck.getRed();
		this.green = deck.getGreen();
		this.blue = deck.getBlue();
		this.colorless = deck.getColorless();
		this.theme = deck.getTheme();
		this.active = deck.getActive();
		this.created = deck.getDateCreated();
		this.winrate = winrate;
		this.games = games;
	}
	
}
