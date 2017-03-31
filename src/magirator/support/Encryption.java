package magirator.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Encryption {

	public static String get_SHA_512(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
	
    public static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
    
    public static String generateResetCode(){
    	String code = "";
    	
    	String[] words = new String[]{
    			"banana", "monkey", "donkey", "soup", "sorcery", "instant", "creature", "madness", "heroic", "dredge", "camel", "wine", "artifact", "flying", "bear", 
    			"book", "drink", "spaceman", "astronaut", "engine", "mana", "first", "lifelink", "equipment", "ladder", "adder", "darth", "spider", "pig", "discard", 
    			"juice", "carrot", "piston", "3", "7", "head", "nose", "margin", "rise", "ride", "oyster", "misspeld", "wine", "123", "45", "99", "0", "freeze", "black", 
    			"white", "red", "green", "blue", "colorless", "colorfull", "pauper", "standard", "vintage", "modern", "commander", "limited", "but", "bed", "orange", 
    			"orangutang", "chaos", "order", "devil", "angel", "potato", "yarn", "space", "spice", "herb", "tree", "plant", "token", "whenever", "forever", "innistrad", 
    			"tarkir", "tank", "bullet", "helmet", "spine", "secret", "code", "and", "morot", "mask", "kanin", "groda", "bastu", "sauna", "hemd", "geist", "zombie", 
    			"ape", "blau", "bly", "kort", "skeleton", "jar", "power", "maus", "katze", "grind", "fastna", "hjort", "ska", "vill", "fluga", "spindel", "kanon", "huge",
                "age", "boat", "cure", "das", "evil", "fauna", "ghost", "hause", "indigo", "jerk", "klein", "lost", "mein", "note", "open", "panic", "query", "run",
                "style", "tail", "undead", "villain", "xtra", "yes", "zebra", "uneven", "33", "lone", "wolf", "bank", "skruv", "lastbil", "useful", "card", 
                "pizza", "mojna", "skostorlek", "wither", "exalted", "extort", "theros", "ravnica", "7th", "10th", "cool", "gorgon", "zendikar", "kamigawa", "end" 
    	};

        Random random = new Random();
        
        while (code.length() < 14){
        	code += words[random.nextInt(words.length)];       	
        }
    	
    	return code;
    }
}
