package magirator.dataobjects;

import java.util.Map;

import com.google.gson.JsonObject;

import magirator.interfaces.IPlayer;

public class Minion implements IPlayer {
	
	private int id;
	private String name;
	
	public Minion(JsonObject json) {
		this.name = json.get("playername").getAsString();
		this.id = json.get("id").getAsInt();
	}

	public Minion(int id, Map playerMap) {
		this.id = id;
		this.name = (String) playerMap.get("name");
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
