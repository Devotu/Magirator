package magirator.view;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Condition {

	private String condition;
	private String subject;
	private String value;

	public Condition(JsonObject o) throws JsonParseException {
		this.condition = o.get("condition").getAsString();
		this.subject = o.get("subject").getAsString();
		this.value = o.get("value").getAsString();
	}

	public String getCondition() {
		return condition;
	}

	public String getSubject() {
		return subject;
	}

	public String getValue() {
		return value;
	}
}
