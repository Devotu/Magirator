package magirator.dataobjects;

import java.util.Map;

import com.google.gson.JsonObject;

public class Player {

	private int id;
	private String playername;
	
	public Player(JsonObject json) {
		this.playername = json.get("playername").getAsString();
		this.id = json.get("id").getAsInt();
	}

	public Player(int id, Map playerMap) {
		this.id = id;
		this.playername = (String) playerMap.get("name");
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getPlayerName() {
		return playername;
	}
	

	//Should no longer be used
	public Player(String name, int id) {
		this.playername = name;
		this.id = id;
	}

	public void setName(String name) {
		this.playername = name;
	}

	public void setId(int i){
		this.id = i;
	}

}
