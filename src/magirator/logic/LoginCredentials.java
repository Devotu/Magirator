package magirator.logic;

import java.security.NoSuchAlgorithmException;

import com.google.gson.JsonObject;

import magirator.support.Encryption;

public class LoginCredentials {
	
	private static final int MIN_PASSWORD = 8;
	
	private String username;
	private String password;
	private String retype;
	private String code;
	

	public LoginCredentials(JsonObject json) {
		this.username = json.has("username") ? json.get("username").getAsString().toLowerCase() : null;
		this.password = json.has("password") ? json.get("password").getAsString() : null;
		this.retype = json.has("retype") ? json.get("retype").getAsString() : null;
		this.code = json.has("code") ? json.get("code").getAsString() : null;
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
	
	public String getCode() {
		return code;
	}
	
	public boolean isValidLogin(){
		if(username == null || password == null || password.length() < 8){
			return false;
		}
		return true;
	}
	
	public void encryptPassword() throws NoSuchAlgorithmException{
		this.password = Encryption.get_SHA_512(this.password);
	}
}
