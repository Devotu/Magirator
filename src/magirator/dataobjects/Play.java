package magirator.dataobjects;

import java.util.Map;

public class Play {
	
	private int id;
	private int place;
	private boolean confirmed;
	private String comment;
	
	public Play(int playId, Map playMap) {
		this.id = playId;
		this.place = (Integer) playMap.get("place");
		this.confirmed = ((Integer)playMap.get("confirmed") == 1 ? true : false);
		this.comment = (String)playMap.get("comment");
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

}
