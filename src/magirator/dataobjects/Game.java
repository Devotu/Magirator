package magirator.dataobjects;

import java.util.Date;
import java.util.Map;

public class Game {
	
	private int id;
	private Date datePlayed;//TODO Remove
	private Date created;
	
	public Game(Map gameMap) {
		this.datePlayed = (Date)gameMap.get("crated");
	}

	public Game(int id, Map game) {
		this.id = id;
		
		long longTime = (Long)game.get("created");
		this.created = (new Date(longTime));
	}

	public int getId() {
		return id;
	}
	
	public Date getCreated() {
		return created;
	}
	
	
	

	//TODO remove
	public void setId(int id) {
		this.id = id;
	}

	public Date getDatePlayed() {
		return datePlayed;
	}

	public void setDatePlayed(Date datePlayed) {
		this.datePlayed = datePlayed;
	}
}
