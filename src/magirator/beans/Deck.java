package magirator.beans;

import java.util.Date;

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
	
	public Deck (){ }
	
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

	public String getCreated() {
		return this.created.toString();
	}
	
}
