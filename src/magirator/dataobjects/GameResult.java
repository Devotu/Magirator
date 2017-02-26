package magirator.dataobjects;

import java.util.List;

public class GameResult {

	private Game game;
	private List<OldResult> results;

	public GameResult(Game game, List<OldResult> results) {
		super();
		this.game = game;
		this.results = results;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<OldResult> getResults() {
		return results;
	}

	public void setResults(List<OldResult> results) {
		this.results = results;
	}
	
	public void addResult(OldResult result){
		this.results.add(result);
	}
	
}
