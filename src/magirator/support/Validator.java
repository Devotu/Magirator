package magirator.support;

import magirator.viewobjects.LoginCredentials;

public class Validator {
	
	public static boolean hasValidResetCredentials(LoginCredentials l){
		if (l.getUsername() != null && l.getPassword() != null && l.getRetype() != null && l.getCode() != null){
			return true;
		}
		return false;
	}

}
