package magirator.viewobjects;

import com.google.gson.JsonObject;

public class LoginCredentials {
	
	private String username;
	private String password;
	

	public LoginCredentials(JsonObject json) {
		this.username = json.get("username").getAsString();
		this.password = json.get("password").getAsString();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
