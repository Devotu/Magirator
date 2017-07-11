package magirator.data.entities;

import java.util.Map;

import com.google.gson.JsonObject;

public class Player {

	private int id;
	private String name;
	
	public Player(JsonObject json) {
		this.name = json.get("name").getAsString();
		this.id = json.get("id").getAsInt();
	}

	public Player(Map<String, ?> properties) {
		this.id = Math.toIntExact((long) properties.get("id"));
		this.name = (String) properties.get("name");
	}

	public int getId(){
		return this.id;
	}
	
	public String getName() {
		return name;
	}

	/** @return :Player { id:?, name:? }
	 */
	public static String neoCreator() {
		return ":Player { id:?, name:? }";
	}
}
