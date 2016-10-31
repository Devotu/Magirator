package magirator.beans;

import java.util.List;

public class GameResult {

	private Game game;
	private List<Result> results;

	public GameResult(Game game, List<Result> results) {
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

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}
	
	public void addResult(Result result){
		this.results.add(result);
	}
	
}
