package magirator.view;

import com.google.gson.JsonObject;

public class PlayerName {
	
	private String playername;
	
	public PlayerName(JsonObject json) {
		this.playername = json.get("playername").getAsString();
	}
	
	public String getPlayerName() {
		return playername;
	}

}
