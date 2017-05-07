package magirator.data.entities;

import java.util.Date;
import java.util.Map;

import com.google.gson.JsonObject;

public class Deck {

	private int deckid;
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
	
	
	public Deck(int id, Map deckMap) throws Exception {
		this.deckid = id; //Hur får jag med korrekt?
		this.name = (String)deckMap.get("name");
		this.format = (String)deckMap.get("format");
		this.blackCards = (long)deckMap.get("black");
		this.whiteCards = (long)deckMap.get("white");
		this.redCards = (long)deckMap.get("red");
		this.greenCards = (long)deckMap.get("green");
		this.blueCards = (long)deckMap.get("blue");
		this.colorlessCards = (long)deckMap.get("colorless");
		this.theme = (String)deckMap.get("theme");
		this.active = (boolean)deckMap.get("active");
		
		long longTime = (Long)deckMap.get("created");
		this.created = new Date(longTime);
	}
	
	
	public Deck(JsonObject deck) {
		this.deckid = (deck.has("id") ? deck.get("id").getAsNumber() : 0).intValue();
		this.name = deck.get("name").getAsString();
		this.format = deck.get("format").getAsString();
		this.theme = (deck.has("theme") ? deck.get("theme").getAsString() : "");
		this.active = (deck.has("active") ? deck.get("active").getAsBoolean() : true );
				
		if (deck.has("black")){ this.blackCards = deck.get("black").getAsNumber().longValue(); };
		if (deck.has("white")){ this.whiteCards = deck.get("white").getAsNumber().longValue(); };
		if (deck.has("red")){ this.redCards = deck.get("red").getAsNumber().longValue(); };
		if (deck.has("green")){ this.greenCards = deck.get("green").getAsNumber().longValue(); };
		if (deck.has("blue")){ this.blueCards = deck.get("blue").getAsNumber().longValue(); };
		if (deck.has("colorless")){ this.colorlessCards = deck.get("colorless").getAsNumber().longValue(); };
		
		long longTime = deck.get("created").getAsLong();
		this.created = new Date(longTime);
	}

	//Set
	public void setDeckid(int deckid) {
		this.deckid = deckid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setBlack(int black) {
		this.blackCards = black;
	}
	
	public void setWhite(int white) {
		this.whiteCards = white;
	}
	
	public void setRed(int red) {
		this.redCards = red;
	}
	
	public void setGreen(int green) {
		this.greenCards = green;
	}
	
	public void setBlue(int blue) {
		this.blueCards = blue;
	}
	
	public void setColorless(int colorless) {
		this.colorlessCards = colorless;
	}
	
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setDate(Date created) {
		this.created = created;
	}
	
	//Get
	public int getDeckid() {
		return this.deckid;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFormat() {
		return this.format;
	}
	
	public long getBlackCards() {
		return this.blackCards;
	}
	
	public long getWhiteCards() {
		return this.whiteCards;
	}
	
	public long getRedCards() {
		return this.redCards;
	}
	
	public long getGreenCards() {
		return this.greenCards;
	}
	
	public long getBlueCards() {
		return this.blueCards;
	}
	
	public long getColorlessCards() {
		return this.colorlessCards;
	}
	
	public String getTheme() {
		return this.theme;
	}

	public boolean getActive() {
		return this.active;
	}
	
	public Date getDateCreated() {
		return this.created;
	}

	public String getCreated() {
		return this.created.toString();
	}
	
}
