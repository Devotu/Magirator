package magirator.data.collections;

/**
 * @author ottu
 * Information containing the status of a given player at a given time
 */
public class PlayerStatus {
	
	private int id;
	private int life;
	private boolean confirmed;
	
	
	public PlayerStatus(int id, int life, boolean confirmed) {
		this.id = id;
		this.life = life;
		this.confirmed = confirmed;
	}		
}
