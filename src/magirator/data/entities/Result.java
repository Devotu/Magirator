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
	
	public Result(Map<String, ?> properties) {
		this.id = Math.toIntExact((long) properties.get("id"));
		this.place = Integer.valueOf( properties.get("place").toString() );
		this.confirmed = (boolean) (properties.get("confirmed"));
		this.comment = (String)properties.get("comment");
		
		long longTime = (Long)properties.get("added");
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
	
	
	/** @return :Result {id:?, place: ?, comment: ?, confirmed: ?, added: TIMESTAMP() }
	 */
	public static String neoCreator() {
		return ":Result {id:?, place: ?, comment: ?, confirmed: ?, added: TIMESTAMP() }";
	}

}
