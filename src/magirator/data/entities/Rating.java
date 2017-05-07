package magirator.data.entities;

import java.util.Map;

import com.google.gson.JsonObject;

public class Rating {
	
	private int id;
	private int speed;
	private int strength;
	private int synergy;
	private int control;
	
	public Rating(JsonObject rating) {
		this.speed = rating.has("speed") ? rating.get("speed").getAsInt() : 0;
		this.strength = rating.has("strength") ? rating.get("strength").getAsInt() : 0;
		this.synergy = rating.has("synergy") ? rating.get("synergy").getAsInt() : 0;
		this.control = rating.has("control") ? rating.get("control").getAsInt() : 0;
	}
	
	public Rating(int ratingId, Map properties) {
		this.id = ratingId;
		this.speed = (int)(long)properties.get("speed");
		this.strength = (int)(long)properties.get("strength");
		this.synergy = (int)(long)properties.get("synergy");
		this.control = (int)(long)properties.get("control");
	}
	
	public int getId() {
		return id;
	}

	public int getSpeed() {
		return speed;
	}

	public int getStrength() {
		return strength;
	}

	public int getSynergy() {
		return synergy;
	}

	public int getControl() {
		return control;
	}
}
