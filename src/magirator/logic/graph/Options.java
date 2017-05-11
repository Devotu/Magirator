package magirator.logic.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Options {

	public static JsonArray getTypes(){
		
		JsonArray types = new JsonArray();
    	types.add("winrate");
    	types.add("colorbars"); 
    	
    	return types;
	}

	public static JsonArray getConditions() {

		JsonArray conditions = new JsonArray();
		
    	JsonArray subjects = new JsonArray();
    	subjects.add("player");
    	subjects.add("format");
    	subjects.add("color");
    	JsonObject against = new JsonObject();
    	against.add("against", subjects);
    	conditions.add(against);
    	
    	subjects = new JsonArray();
    	subjects.add("format");
    	subjects.add("color");
    	JsonObject with = new JsonObject();
    	with.add("with", subjects);
    	conditions.add(with);
    	
    	subjects = new JsonArray();
    	subjects.add("today");
    	subjects.add("weekend");
    	JsonObject time = new JsonObject();
    	time.add("time", subjects);
    	conditions.add(time);
    	
    	return conditions;    	
	}
}
