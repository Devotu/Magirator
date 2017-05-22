package magirator.data.collections;

import java.util.ArrayList;
import java.util.List;

import magirator.data.entities.Game;

/**
 * Would have been just Game if it was not taken by the data object
 * Feel free to refactor if a more suitable name can be found
 * @author ottu
 */
public class GameBundle {
	
	private Game game;
	private PlayerGameResult self;
	private List<PlayerGameResult> opponents;

	public GameBundle(Game game) {
		this.game = game;
		this.opponents = new ArrayList<>();
	}
	
	public void addOpponent(PlayerGameResult p){
		this.opponents.add(p);
	}
	
	public void addSelf(PlayerGameResult p){
		this.self = p;
	}
	
	public boolean isSameGame(PlayerGameResult p){
		if (this.game.getId() == p.getGame().getId()) {
			return true;
		} else {
			return false;
		}
	}
	
	public Game getGame(){
		return this.game;
	}

	public boolean isEmpty() {
		if (this.opponents.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public List<PlayerGameResult> getOpponents() {
		return opponents;
	}

	public PlayerGameResult getSelf() {
		return self;
	}
	
	
}
