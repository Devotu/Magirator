package magirator.beans;

import java.util.Map;

public class Play {
	
	private int place;
	private int confirmed;
	private String comment;
	
	public Play(Map playMap) {
		this.place = ((Long)playMap.get("place")).intValue();
		this.confirmed = ((Long)playMap.get("confirmed")).intValue();
		this.comment = (String)playMap.get("comment");
	}
	
	public Play(int place, boolean confirmed, String comment) {
		this.place = place;
		this.confirmed = confirmed ? 1 : 0;
		this.comment = comment;
	}

	public int getPlace() {
		return place;
	}
	public void setPlace(int place) {
		this.place = place;
	}
	
	public int getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

}
