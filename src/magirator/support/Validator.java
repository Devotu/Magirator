package magirator.support;

import magirator.dataobjects.Deck;
import magirator.dataobjects.Minion;
import magirator.viewobjects.LoginCredentials;

public class Validator {
	
	public static boolean hasValidResetCredentials(LoginCredentials l){
		if (l.getUsername() != null && l.getPassword() != null && l.getRetype() != null && l.getCode() != null){
			if (l.getPassword().equals(l.getRetype())){
				return true;
			}
		}
		return false;
	}

	public static boolean isValidMinion(Minion m){
		validate: {
			if( "".equals( m.getName() ) ){
				break validate;
			}
			
			return true;
		}

		return false;
	}
	
	public static boolean isRegisterdMinion(Minion m){
		validate: {
			if( 0 == m.getId() ){
				break validate;
			}
			if( "".equals( m.getName() ) ){
				break validate;
			}
			
			return true;
		}

		return false;
	}

	public static boolean isValidDeck(Deck d) {

		validate: {
			if( "".equals( d.getName() ) ){
				break validate;
			}
			if( "".equals( d.getFormat() ) ){
				break validate;
			}
			if( "".equals( d.getDateCreated() ) ){
				break validate;
			}
		
			return true;
		}
	
		return false;
	}
}
