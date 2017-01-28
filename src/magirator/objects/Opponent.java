package magirator.objects;

import java.util.HashMap;
import java.util.Objects;

public class Opponent {
	
	private String name;
	private int id;
	private HashMap<String, Integer> decks;
	private String deckArray;
		
	public Opponent() {
		this.decks = new HashMap<String, Integer>();
		this.deckArray = "[]";
	}

	@Override
	public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Opponent)) {
            return false;
        }
        Opponent opponent = (Opponent) o;
        return this.id == opponent.getId();
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash(name, id, decks);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public HashMap<String, Integer> getDecks() {
		return decks;
	}
	public void setDecks(HashMap<String, Integer> decks) {
		this.decks = decks;
	}
	
	public void addDeck(String name, Integer id){
		this.decks.put(name, id);
	}

	public String getDeckArray() {
		return deckArray;
	}

	public void setDeckArray(String deckArray) {
		this.deckArray = deckArray;
	}
	
	public void addDeckString(String name, Integer id){
		this.deckArray = this.deckArray.substring(0, this.deckArray.length()-1); //Remove end bracket
		if	(!"[".equals(this.deckArray)){//Array is not empty
			this.deckArray = this.deckArray += ", "; //Add separator
		}
		this.deckArray = this.deckArray += "{\"name\":\"" + name + "\", \"id\":" + id + "}]"; //Add new content
	}

}
