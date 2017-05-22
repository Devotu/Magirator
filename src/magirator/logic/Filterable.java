package magirator.logic;

import java.util.Date;

import magirator.data.collections.PlayerGameResult;
import magirator.data.entities.Deck;
import magirator.data.entities.Game;
import magirator.data.interfaces.IPlayer;

/**
 * Contains values that can be filtered on
 * Each value must have:
 * 	- initiation from a magirator.data.entities class
 *  - a boolean method to query if it exists
 *  - getter
 * @author ottu
 */
public class Filterable {

	private int playerId;
	private String format;
	private Date date;
	
	
	public Filterable(IPlayer player) {
		playerId = player.getId();
	}
	
	public Filterable(Deck deck) {
		format = deck.getFormat();
	}
	
	public Filterable(Game game) {
		date = game.getCreated();
	}	
	
	public Filterable(PlayerGameResult participant) {
		playerId = participant.getPlayer().getId();
		format = participant.getDeck().getFormat();
		date = participant.getGame().getCreated();
	}
	

	public boolean playerIdExist(){
		if (playerId != 0) { return true; } return false;
	}
	
	public boolean formatExist(){
		if (format != null) { return true; } return false;
	}
	
	public boolean dateExist(){
		if (date != null) { return true; } return false;
	}
	

	public int getPlayerId() {
		return playerId;
	}

	public String getFormat() {
		return format;
	}

	public Date getDate() {
		return date;
	}
	
}
