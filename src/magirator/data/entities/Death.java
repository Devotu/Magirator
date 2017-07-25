package magirator.data.entities;

import java.util.Map;

public class Death {
	
	private int id;

	public Death(Map<String, ?> properties) {
		this.id = Math.toIntExact((long) properties.get("id"));
	}

	public int getId() {
		return id;
	}
	
	
	/** @return :Death {id:?, added: TIMESTAMP()}
	 */
	public static String neoCreator() {
		return ":Death {id:?, added: TIMESTAMP()}";
	}
}
