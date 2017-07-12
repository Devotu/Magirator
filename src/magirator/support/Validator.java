package magirator.support;

import magirator.data.entities.Deck;
import magirator.data.interfaces.Player;
import magirator.logic.LoginCredentials;

public class Validator {
	
	public static boolean hasValidResetCredentials(LoginCredentials l){
		if (l.getUsername() != null && l.getPassword() != null && l.getRetype() != null && l.getCode() != null){
			if (l.getPassword().equals(l.getRetype())){
				return true;
			}
		}
		return false;
	}

	public static boolean isValidPlayer(Player p){
		validate: {
			if( "".equals( p.getName() ) ){
				break validate;
			}
			
			return true;
		}

		return false;
	}
	
	public static boolean isRegisterdPlayer(Player p){
		validate: {
			if( 0 == p.getId() ){
				break validate;
			}
			if( "".equals( p.getName() ) ){
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
