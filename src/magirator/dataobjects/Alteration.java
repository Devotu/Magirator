package magirator.dataobjects;

import java.util.Date;

/**
 * 
 * @author ottu
 * An Alteration is a difference between two decks with a comment.
 */
public class Alteration {

	private String nameWas;
	private String formatWas;
	private boolean blackWas;
	private boolean whiteWas;
	private boolean redWas;
	private boolean greenWas;
	private boolean blueWas;
	private boolean colorlessWas;
	private String themeWas;
	private int colorPatternWas;
	
	private String nameIs;
	private String formatIs;
	private boolean blackIs;
	private boolean whiteIs;
	private boolean redIs;
	private boolean greenIs;
	private boolean blueIs;
	private boolean colorlessIs;
	private String themeIs;
	private int colorPatternIs;
	
	private Date altered;
	private String comment;
	
	public Alteration(Deck previousDeck, Deck currentheck, String comment) throws Exception {
		
		this.nameWas = previousDeck.getName();
		this.formatWas = previousDeck.getFormat();
		this.blackWas = previousDeck.getBlack();
		this.whiteWas = previousDeck.getWhite();
		this.redWas = previousDeck.getRed();
		this.greenWas = previousDeck.getGreen();
		this.blueWas = previousDeck.getBlue();
		this.colorlessWas = previousDeck.getColorless();
		this.themeWas = previousDeck.getTheme();
		this.colorPatternWas = (blackWas? 1:0) + (whiteWas? 2:0) + (redWas? 4:0) + (greenWas? 8:0) + (blueWas? 16:0) + (colorlessWas? 32:0);
		
		this.nameIs = currentheck.getName();
		this.formatIs = currentheck.getFormat();
		this.blackIs = currentheck.getBlack();
		this.whiteIs = currentheck.getWhite();
		this.redIs = currentheck.getRed();
		this.greenIs = currentheck.getGreen();
		this.blueIs = currentheck.getBlue();
		this.colorlessIs = currentheck.getColorless();
		this.themeIs = currentheck.getTheme();
		this.colorPatternIs = (blackIs? 1:0) + (whiteIs? 2:0) + (redIs? 4:0) + (greenIs? 8:0) + (blueIs? 16:0) + (colorlessIs? 32:0);
		
		this.altered = currentheck.getDateCreated();
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

	public boolean isBlackWas() {
		return blackWas;
	}

	public void setBlackWas(boolean blackWas) {
		this.blackWas = blackWas;
	}

	public boolean isWhiteWas() {
		return whiteWas;
	}

	public void setWhiteWas(boolean whiteWas) {
		this.whiteWas = whiteWas;
	}

	public boolean isRedWas() {
		return redWas;
	}

	public void setRedWas(boolean redWas) {
		this.redWas = redWas;
	}

	public boolean isGreenWas() {
		return greenWas;
	}

	public void setGreenWas(boolean greenWas) {
		this.greenWas = greenWas;
	}

	public boolean isBlueWas() {
		return blueWas;
	}

	public void setBlueWas(boolean blueWas) {
		this.blueWas = blueWas;
	}

	public boolean isColorlessWas() {
		return colorlessWas;
	}

	public void setColorlessWas(boolean colorlessWas) {
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

	public boolean isBlackIs() {
		return blackIs;
	}

	public void setBlackIs(boolean blackIs) {
		this.blackIs = blackIs;
	}

	public boolean isWhiteIs() {
		return whiteIs;
	}

	public void setWhiteIs(boolean whiteIs) {
		this.whiteIs = whiteIs;
	}

	public boolean isRedIs() {
		return redIs;
	}

	public void setRedIs(boolean redIs) {
		this.redIs = redIs;
	}

	public boolean isGreenIs() {
		return greenIs;
	}

	public void setGreenIs(boolean greenIs) {
		this.greenIs = greenIs;
	}

	public boolean isBlueIs() {
		return blueIs;
	}

	public void setBlueIs(boolean blueIs) {
		this.blueIs = blueIs;
	}

	public boolean isColorlessIs() {
		return colorlessIs;
	}

	public void setColorlessIs(boolean colorlessIs) {
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

	public int getColorPatternWas() {
		return colorPatternWas;
	}

	public void setColorPatternWas(int colorPatternWas) {
		this.colorPatternWas = colorPatternWas;
	}

	public int getColorPatternIs() {
		return colorPatternIs;
	}

	public void setColorPatternIs(int colorPatternIs) {
		this.colorPatternIs = colorPatternIs;
	}

}