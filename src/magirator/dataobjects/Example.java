package magirator.dataobjects;

import com.google.gson.JsonObject;

public class Example {

	private String text;
	
	public Example(JsonObject json) {
		
		this.text = json.get("test").getAsString();
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
