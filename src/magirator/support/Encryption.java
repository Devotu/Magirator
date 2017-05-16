package magirator.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class Encryption {
	
	private static int RESET_MIN_LENGTH = 14;
	private static int LIVE_TOKEN_ARITY = 4;
	
	private static String[] WORDS = new String[]{
			"banana", "monkey", "donkey", "soup", "sorcery", "instant", "creature", "madness", "heroic", "dredge", "camel", "wine", "artifact", "flying", "bear", 
			"book", "drink", "spaceman", "astronaut", "engine", "mana", "first", "lifelink", "equipment", "ladder", "adder", "darth", "spider", "pig", "discard", 
			"juice", "carrot", "piston", "3", "7", "head", "nose", "margin", "rise", "ride", "oyster", "misspeld", "wine", "123", "45", "99", "0", "freeze", "black", 
			"white", "red", "green", "blue", "colorless", "colorfull", "pauper", "standard", "vintage", "modern", "commander", "limited", "but", "bed", "orange", 
			"orangutang", "chaos", "order", "devil", "angel", "potato", "yarn", "space", "spice", "herb", "tree", "plant", "token", "whenever", "forever", "innistrad", 
			"tarkir", "tank", "bullet", "helmet", "spine", "secret", "code", "and", "morot", "mask", "kanin", "groda", "bastu", "sauna", "hemd", "geist", "zombie", 
			"ape", "blau", "bly", "kort", "skeleton", "jar", "power", "maus", "katze", "grind", "fastna", "hjort", "ska", "vill", "fluga", "spindel", "kanon", "huge",
            "age", "boat", "cure", "das", "evil", "fauna", "ghost", "hause", "indigo", "jerk", "klein", "lost", "mein", "note", "open", "panic", "query", "run",
            "style", "tail", "undead", "villain", "xtra", "yes", "zebra", "uneven", "33", "lone", "wolf", "bank", "skruv", "lastbil", "useful", "card", 
            "pizza", "mojna", "skostorlek", "wither", "exalted", "extort", "theros", "ravnica", "7th", "10th", "cool", "gorgon", "zendikar", "kamigawa", "end",
            "kval", "snooze", "elixir", "statue", "tank", "steve", "vader", "dinosaur", "kid", "grass", "hippo", "club", "cup", "lace", "mask", "erdwal", "attack",
            "mammoth", "bar", "color", "while", "paste", "tooth", "neonate", "eternal", "final", "gorgon", "head", "tail", "sting", "prodigal", "wojek", "cultist"
	};
	

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
    	
    	return generateRandomStringOfLength(RESET_MIN_LENGTH);
    }
    
    public static String generateLiveToken(){
    	
    	return generateRandomStringOfArity(LIVE_TOKEN_ARITY);
    }
    
    
    private static String generateRandomStringOfLength(int minLength){
    	
    	String code = "";
        Random random = new Random();
        
        while (code.length() < minLength){
        	code += WORDS[random.nextInt(WORDS.length)];       	
        }
    	
    	return code;
    }
    
    private static String generateRandomStringOfArity(int minArity){
    	
    	String code = "";
    	int numberOfWords = 0;
        Random random = new Random();
        
        while (numberOfWords < minArity){
        	code += WORDS[random.nextInt(WORDS.length)];
        	numberOfWords++;
        }
    	
    	return code;
    }
}
