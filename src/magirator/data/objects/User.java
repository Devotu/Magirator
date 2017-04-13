package magirator.data.objects;

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
	
	public User(int id, Map properties) {
		this.id = id;
		this.username = (String) properties.get("name");
		this.password = (String) properties.get("password");
	}
	
	public User(int id) {
		this.id = id;
		this.username = null;
		this.password = null;
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
}
