package magirator.data.objects;

import java.util.Date;
import java.util.Map;

public class Game {
	
	private int id;
	private Date datePlayed;//TODO Remove
	private Date created;
	private boolean draw;
	
	public Game(Map gameMap) {
		long longTime = (Long)gameMap.get("created");
		this.datePlayed = new Date(longTime);
		this.draw = (Boolean)gameMap.get("draw");
	}

	public Game(int id, Map game) {
		this.id = id;
		this.draw = (Boolean)game.get("draw");
		
		long longTime = (Long)game.get("created");
		this.created = (new Date(longTime));
	}

	public int getId() {
		return id;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public boolean getDraw(){
		return draw;
	}
	
	
	

	//TODO remove
	/**
	 * @deprecated
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public Date getDatePlayed() {
		return datePlayed;
	}

	/**
	 * @deprecated
	 * @param datePlayed
	 */
	public void setDatePlayed(Date datePlayed) {
		this.datePlayed = datePlayed;
	}
}
