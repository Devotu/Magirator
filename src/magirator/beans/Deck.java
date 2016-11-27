package magirator.beans;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

public class Deck {

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
	
	//TODO remove
	public Deck (int deckid, 
					String name, 
					String format, 
					boolean black, 
					boolean white, 
					boolean red, 
					boolean green, 
					boolean blue, 
					boolean colorless, 
					String theme, 
					boolean active, 
					long created) {
		this.deckid = deckid;
		this.name = name;
		this.format = format;
		this.black = black;
		this.white = white;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.colorless = colorless;
		this.theme = theme;
		this.active = active;
		this.created = new Date(created);
	}
	
	public Deck(int id, Map deckMap) throws Exception {
		this.deckid = id; //Hur får jag med korrekt?
		this.name = (String)deckMap.get("name");
		this.format = (String)deckMap.get("format");
		this.black = (boolean)deckMap.get("black");
		this.white = (boolean)deckMap.get("white");
		this.red = (boolean)deckMap.get("red");
		this.green = (boolean)deckMap.get("green");
		this.blue = (boolean)deckMap.get("blue");
		this.colorless = (boolean)deckMap.get("colorless");
		this.theme = (String)deckMap.get("theme");
		this.active = (boolean)deckMap.get("active");
		
		long longTime = (Long)deckMap.get("created");
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
	
	public void setBlack(boolean black) {
		this.black = black;
	}
	
	public void setWhite(boolean white) {
		this.white = white;
	}
	
	public void setRed(boolean red) {
		this.red = red;
	}
	
	public void setGreen(boolean green) {
		this.green = green;
	}
	
	public void setBlue(boolean blue) {
		this.blue = blue;
	}
	
	public void setColorless(boolean colorless) {
		this.colorless = colorless;
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
	
	public boolean getBlack() {
		return this.black;
	}
	
	public boolean getWhite() {
		return this.white;
	}
	
	public boolean getRed() {
		return this.red;
	}
	
	public boolean getGreen() {
		return this.green;
	}
	
	public boolean getBlue() {
		return this.blue;
	}
	
	public boolean getColorless() {
		return this.colorless;
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
