package magirator.beans;

import java.util.HashMap;
import java.util.Objects;

public class Opponent {
	
	private String name;
	private int id;
	private HashMap<String, Integer> decks;
		
	public Opponent() {
		this.decks = new HashMap<String, Integer>();
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

}
