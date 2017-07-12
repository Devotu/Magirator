package magirator.data.collections;

import magirator.data.entities.Game;
import magirator.data.interfaces.Player;

public class IPlayerGame {
	
	private Player player;
	private Game game;
	
	public IPlayerGame(Player player, Game game) {
		super();
		this.player = player;
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public Game getGame() {
		return game;
	}
}
