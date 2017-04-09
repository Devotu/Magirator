package magirator.dataobjects;

import java.util.Map;

import com.google.gson.JsonObject;

import magirator.interfaces.IPlayer;

public class Minion implements IPlayer {
	
	private int id;
	private String name;
	
	public Minion(JsonObject json) {
		this.name = json.get("name").getAsString();
		this.id = json.has("id") ? json.get("id").getAsInt() : 0;
	}

	public Minion(int id, Map minionMap) {
		this.id = id;
		this.name = (String) minionMap.get("name");
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
