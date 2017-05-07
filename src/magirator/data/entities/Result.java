package magirator.data.entities;

import java.util.Date;
import java.util.Map;

import com.google.gson.JsonObject;

public class Result {
	
	private int id;
	private int place;
	private boolean confirmed;
	private String comment;
	private Date added;
	
	public Result(int resultId, Map resultMap) {
		this.id = resultId;
		this.place = Integer.valueOf( resultMap.get("place").toString() );
		this.confirmed = (Integer.valueOf( resultMap.get("confirmed").toString() ) == 1 ? true : false);
		this.comment = (String)resultMap.get("comment");
		
		long longTime = (Long)resultMap.get("added");
		this.added = (new Date(longTime));
	}
	
	public Result(JsonObject result) {
		this.id = result.has("id") ? result.get("id").getAsInt() : 0;
		this.place = result.has("place") ? result.get("place").getAsInt() : 0;
		this.confirmed = result.has("confirmed") ? result.get("confirmed").getAsBoolean() : false;
		this.comment = result.has("comment") ? result.get("comment").getAsString() : "";
		
		long longTime = result.get("added").getAsLong();
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

}
