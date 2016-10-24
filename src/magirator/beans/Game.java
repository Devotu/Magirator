package magirator.beans;

import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private int id;
	private String datePlayed;
	private List<GameResult> results;

	public Game() {
		this.results = new ArrayList<GameResult>();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDatePlayed() {
		return datePlayed;
	}

	public void setDatePlayed(String datePlayed) {
		this.datePlayed = datePlayed;
	}

	public List<GameResult> getResults() {
		return results;
	}

	public void setResults(List<GameResult> results) {
		this.results = results;
	}

}
