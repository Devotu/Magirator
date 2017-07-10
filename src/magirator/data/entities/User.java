package magirator.data.entities;

import java.util.Map;

import com.google.gson.JsonObject;

public class User {
	
	private int id;
	private String username;
	private String password;

	public User(JsonObject json) {
		this.username = json.get("username").getAsString();
		this.password = json.get("password").getAsString();
	}
	
	public User(Map<String, ?> properties) {
		this.id = Math.toIntExact((long) properties.get("id"));
		this.username = (String) properties.get("name");
		this.password = (String) properties.get("password");
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	/**
	 * @return :User {name:?, password:?, created: TIMESTAMP()}
	 */
	public static String neoCreator(){
		return ":User {id:?, name:?, password:?, created: TIMESTAMP()}";
	}
}
