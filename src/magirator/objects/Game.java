package magirator.objects;

import java.util.Date;
import java.util.Map;

public class Game {
	
	private int id;
	private Date datePlayed;
	
	public Game(Map gameMap) {
		this.datePlayed = (Date)gameMap.get("crated");
	}

	public int getId() {
		return id;
	}

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
