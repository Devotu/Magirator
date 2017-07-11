package magirator.data.entities;

import java.util.Date;
import java.util.Map;

import com.google.gson.JsonObject;

public class Deck {

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
	
	
	public Deck(Map<String, ?> properties) throws Exception {
		this.id = (int) properties.get(id);
		this.name = (String)properties.get("name");
		this.format = (String)properties.get("format");
		this.blackCards = (long)properties.get("black");
		this.whiteCards = (long)properties.get("white");
		this.redCards = (long)properties.get("red");
		this.greenCards = (long)properties.get("green");
		this.blueCards = (long)properties.get("blue");
		this.colorlessCards = (long)properties.get("colorless");
		this.theme = (String)properties.get("theme");
		this.active = (boolean)properties.get("active");
		
		long longTime = (Long)properties.get("created");
		this.created = new Date(longTime);
	}
	
	
	public Deck(JsonObject deck) {
		this.id = (deck.has("id") ? deck.get("id").getAsNumber() : 0).intValue();
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

	public int getId() {
		return this.id;
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
	
	
	/** @return :Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true}
	 */
	public static String neoCreator() {
		return ":Deck {id:?, name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true}";
	}
	
}
