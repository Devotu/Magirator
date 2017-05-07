package magirator.data.entities;

import java.util.Map;

import com.google.gson.JsonObject;

import magirator.data.interfaces.IPlayer;

public class Player implements IPlayer {

	private int id;
	private String name;
	
	public Player(JsonObject json) {
		this.name = json.get("name").getAsString();
		this.id = json.get("id").getAsInt();
	}

	public Player(int id, Map playerMap) {
		this.id = id;
		this.name = (String) playerMap.get("name");
	}
	
	/* (non-Javadoc)
	 * @see magirator.dataobjects.IPlayer#getId()
	 */
	@Override
	public int getId(){
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see magirator.dataobjects.IPlayer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
}
