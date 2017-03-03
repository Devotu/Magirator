package magirator.viewobjects;

import com.google.gson.JsonObject;

public class LoginCredentials {
	
	private String username;
	private String password;
	private String retype;
	

	public LoginCredentials(JsonObject json) {
		this.username = json.get("username").getAsString().toLowerCase();
		this.password = json.get("password").getAsString();
		this.retype = json.has("retype") ? json.get("retype").getAsString() : null;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRetype() {
		return retype;
	}

}
