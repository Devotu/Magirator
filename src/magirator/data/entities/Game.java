package magirator.data.entities;

import java.util.Date;
import java.util.Map;

public class Game {
	
	private int id;
	private Date created;
	private boolean draw;

	public Game(Map<String, ?> properties) {
		this.id = Math.toIntExact((long) properties.get("id"));
		this.draw = properties.containsKey("draw") ? (Boolean)properties.get("draw") : false;
		
		long longTime = (Long)properties.get("created");
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
}
