package magirator.dataobjects;

import java.util.Date;
import java.util.Map;

import com.google.gson.JsonObject;

/**
 * A Play is the connection between a Deck and a Game with a date
 * @author ottu
 *
 */
public class Play {
	
	private int id;
	private int place;
	private boolean confirmed;
	private String comment;
	private Date added;
	
	public Play(int playId, Map playMap) {
		this.id = playId;
		this.place = Integer.valueOf( playMap.get("place").toString() );
		this.confirmed = (Integer.valueOf( playMap.get("confirmed").toString() ) == 1 ? true : false);
		this.comment = (String)playMap.get("comment");
		
		long longTime = (Long)playMap.get("added");
		this.added = (new Date(longTime));
	}
	
	public Play(JsonObject play) {
		this.id = play.has("id") ? play.get("id").getAsInt() : 0;
		this.place = play.has("place") ? play.get("place").getAsInt() : 0;
		this.confirmed = play.has("confirmed") ? play.get("confirmed").getAsBoolean() : false;
		this.comment = play.has("comment") ? play.get("comment").getAsString() : "";
		
		long longTime = play.get("added").getAsLong();
		this.added = new Date(longTime);
	}

	public int getId() {
		return id;
	}

	public int getPlace() {
		return place;
	}
	
	public boolean getConfirmed() {
		return confirmed;
	}
	
	public String getComment() {
		return comment;
	}
	
	public Date getAdded() {
		return added;
	}
	
	

	//TODO Remove
	public Play(int i, int place2, boolean b, String comment2) {
		// TODO Auto-generated constructor stub
	}

	public void setComment(String playerComment) {
		// TODO Auto-generated method stub
		
	}

	public void setConfirmed(int i) {
		// TODO Auto-generated method stub
		
	}

}
