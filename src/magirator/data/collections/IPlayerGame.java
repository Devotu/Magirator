package magirator.data.collections;

import magirator.data.interfaces.IPlayer;
import magirator.data.objects.Game;

public class IPlayerGame {
	
	private IPlayer player;
	private Game game;
	
	public IPlayerGame(IPlayer player, Game game) {
		super();
		this.player = player;
		this.game = game;
	}

	public IPlayer getPlayer() {
		return player;
	}

	public Game getGame() {
		return game;
	}
}
