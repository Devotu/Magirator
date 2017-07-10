package magirator.data.entities;

import java.util.Map;

import com.google.gson.JsonObject;

import magirator.data.interfaces.IPlayer;

/**
 * @author ottu
 * @deprecated
 */
public class Minion implements IPlayer {
	
	private int id;
	private String name;
	
	public Minion(JsonObject json) {
		this.name = json.get("name").getAsString();
		this.id = json.has("id") ? json.get("id").getAsInt() : 0;
	}

	public Minion(Map<String, ?> properties) {
		this.id = (int) properties.get(id);
		this.name = (String) properties.get("name");
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
