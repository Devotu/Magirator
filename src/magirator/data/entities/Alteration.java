package magirator.data.entities;

import java.util.Date;

/**
 * 
 * @author ottu
 * An Alteration is a difference between two decks with a comment.
 */
public class Alteration {

	private int id;
	
	private String nameWas;
	private String formatWas;
	private long blackWas;
	private long whiteWas;
	private long redWas;
	private long greenWas;
	private long blueWas;
	private long colorlessWas;
	private String themeWas;
	private long colorPatternWas;
	
	private String nameIs;
	private String formatIs;
	private long blackIs;
	private long whiteIs;
	private long redIs;
	private long greenIs;
	private long blueIs;
	private long colorlessIs;
	private String themeIs;
	private long colorPatternIs;
	
	private Date altered;
	private String comment;
	
	public Alteration(Deck previousDeck, Deck currentDeck, String comment) throws Exception {
		
		this.id = currentDeck.getDeckid();
		
		this.nameWas = previousDeck.getName();
		this.formatWas = previousDeck.getFormat();
		this.blackWas = previousDeck.getBlackCards();
		this.whiteWas = previousDeck.getWhiteCards();
		this.redWas = previousDeck.getRedCards();
		this.greenWas = previousDeck.getGreenCards();
		this.blueWas = previousDeck.getBlueCards();
		this.colorlessWas = previousDeck.getColorlessCards();
		this.themeWas = previousDeck.getTheme();
		this.colorPatternWas = (blackWas *1) + (whiteWas *2) + (redWas *4) + (greenWas *8) + (blueWas * 16) + (colorlessWas *32);
		
		this.nameIs = currentDeck.getName();
		this.formatIs = currentDeck.getFormat();
		this.blackIs = currentDeck.getBlackCards();
		this.whiteIs = currentDeck.getWhiteCards();
		this.redIs = currentDeck.getRedCards();
		this.greenIs = currentDeck.getGreenCards();
		this.blueIs = currentDeck.getBlueCards();
		this.colorlessIs = currentDeck.getColorlessCards();
		this.themeIs = currentDeck.getTheme();
		this.colorPatternIs = (blackIs *1) + (whiteIs *2) + (redIs *4) + (greenIs *8) + (blueIs *16) + (colorlessIs *32);
		
		this.altered = currentDeck.getDateCreated();
		this.comment = comment;
		
	}

	public String getNameWas() {
		return nameWas;
	}

	public void setNameWas(String nameWas) {
		this.nameWas = nameWas;
	}

	public String getFormatWas() {
		return formatWas;
	}

	public void setFormatWas(String formatWas) {
		this.formatWas = formatWas;
	}

	public long isBlackWas() {
		return blackWas;
	}

	public void setBlackWas(long blackWas) {
		this.blackWas = blackWas;
	}

	public long isWhiteWas() {
		return whiteWas;
	}

	public void setWhiteWas(long whiteWas) {
		this.whiteWas = whiteWas;
	}

	public long isRedWas() {
		return redWas;
	}

	public void setRedWas(long redWas) {
		this.redWas = redWas;
	}

	public long isGreenWas() {
		return greenWas;
	}

	public void setGreenWas(long greenWas) {
		this.greenWas = greenWas;
	}

	public long isBlueWas() {
		return blueWas;
	}

	public void setBlueWas(long blueWas) {
		this.blueWas = blueWas;
	}

	public long isColorlessWas() {
		return colorlessWas;
	}

	public void setColorlessWas(long colorlessWas) {
		this.colorlessWas = colorlessWas;
	}

	public String getthemeWas() {
		return themeWas;
	}

	public void setthemeWas(String themeWas) {
		this.themeWas = themeWas;
	}

	public String getNameIs() {
		return nameIs;
	}

	public void setNameIs(String nameIs) {
		this.nameIs = nameIs;
	}

	public String getFormatIs() {
		return formatIs;
	}

	public void setFormatIs(String formatIs) {
		this.formatIs = formatIs;
	}

	public long isBlackIs() {
		return blackIs;
	}

	public void setBlackIs(long blackIs) {
		this.blackIs = blackIs;
	}

	public long isWhiteIs() {
		return whiteIs;
	}

	public void setWhiteIs(long whiteIs) {
		this.whiteIs = whiteIs;
	}

	public long isRedIs() {
		return redIs;
	}

	public void setRedIs(long redIs) {
		this.redIs = redIs;
	}

	public long isGreenIs() {
		return greenIs;
	}

	public void setGreenIs(long greenIs) {
		this.greenIs = greenIs;
	}

	public long isBlueIs() {
		return blueIs;
	}

	public void setBlueIs(long blueIs) {
		this.blueIs = blueIs;
	}

	public long isColorlessIs() {
		return colorlessIs;
	}

	public void setColorlessIs(int colorlessIs) {
		this.colorlessIs = colorlessIs;
	}

	public String getthemeIs() {
		return themeIs;
	}

	public void setthemeIs(String themeIs) {
		this.themeIs = themeIs;
	}

	public Date getAltered() {
		return altered;
	}

	public void setAltered(Date altered) {
		this.altered = altered;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getColorPatternWas() {
		return colorPatternWas;
	}

	public void setColorPatternWas(long colorPatternWas) {
		this.colorPatternWas = colorPatternWas;
	}

	public long getColorPatternIs() {
		return colorPatternIs;
	}

	public void setColorPatternIs(long colorPatternIs) {
		this.colorPatternIs = colorPatternIs;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}