package magirator.viewobjects;

import java.security.NoSuchAlgorithmException;

import com.google.gson.JsonObject;

import magirator.support.Encryption;

/**
 * Never to leave server side due to security functions
 * @author ottu
 *
 */
public class LoginCredentials {
	
	private String username;
	private String password;
	private String retype;
	private byte[] salt;
	

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
	
	public void encryptPassword() throws NoSuchAlgorithmException{
		this.password = Encryption.get_SHA_512(this.password);
	}
}
