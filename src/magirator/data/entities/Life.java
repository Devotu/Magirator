package magirator.data.entities;

import java.util.Map;

public class Life {
	
	private int id;
	private int life;

	public Life(Map<String, ?> properties) {
		this.id = Math.toIntExact((long) properties.get("id"));
		this.life = Math.toIntExact((long) properties.get("life"));
	}

	public int getId() {
		return id;
	}
	
	public int getLife() {
		return life;
	}
	
	
	/** @return :Life {id:?, added: TIMESTAMP(), life:?}
	 */
	public static String neoCreator() {
		return ":Life {id:?, added: TIMESTAMP(), life:?}";
	}
}
