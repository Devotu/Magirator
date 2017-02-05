package magirator.viewobjects;

import com.google.gson.JsonObject;

public class PublicPlayer {
	
	private String playername;
	
	public PublicPlayer(JsonObject json) {
		this.playername = json.get("playername").getAsString();
	}
	
	public String getPlayerName() {
		return playername;
	}

}
