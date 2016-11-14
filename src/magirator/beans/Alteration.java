package magirator.beans;

import java.util.Date;
import java.util.Map;

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
	
	private String nameIs;
	private String formatIs;
	private boolean blackIs;
	private boolean whiteIs;
	private boolean redIs;
	private boolean greenIs;
	private boolean blueIs;
	private boolean colorlessIs;
	private String themeIs;
	
	private Date altered;
	
	public Alteration(Map deckWasMap, Map deckIsMap) throws Exception {
		
		this.nameWas = (String)deckWasMap.get("name");
		this.formatWas = (String)deckWasMap.get("format");
		this.blackWas = (boolean)deckWasMap.get("black");
		this.whiteWas = (boolean)deckWasMap.get("white");
		this.redWas = (boolean)deckWasMap.get("red");
		this.greenWas = (boolean)deckWasMap.get("green");
		this.blueWas = (boolean)deckWasMap.get("blue");
		this.colorlessWas = (boolean)deckWasMap.get("colorless");
		this.themeWas = (String)deckWasMap.get("theme");
		
		this.nameIs = (String)deckIsMap.get("name");
		this.formatIs = (String)deckIsMap.get("format");
		this.blackIs = (boolean)deckIsMap.get("black");
		this.whiteIs = (boolean)deckIsMap.get("white");
		this.redIs = (boolean)deckIsMap.get("red");
		this.greenIs = (boolean)deckIsMap.get("green");
		this.blueIs = (boolean)deckIsMap.get("blue");
		this.colorlessIs = (boolean)deckIsMap.get("colorless");
		this.themeIs = (String)deckIsMap.get("theme");
		
		this.altered = (Date)deckIsMap.get("creaded");
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

	public String getThemeWas() {
		return themeWas;
	}

	public void setThemeWas(String themeWas) {
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

	public String getThemeIs() {
		return themeIs;
	}

	public void setThemeIs(String themeIs) {
		this.themeIs = themeIs;
	}

	public Date getAltered() {
		return altered;
	}

	public void setAltered(Date altered) {
		this.altered = altered;
	}

}