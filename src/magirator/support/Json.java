package magirator.support;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.restlet.representation.Representation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Json {

	public static JsonObject parseRequestData(HttpServletRequest req) throws IOException{
		
		req.setCharacterEncoding("UTF-8");
		
		StringBuffer sb = new StringBuffer();
		String line = null;
		
	    BufferedReader reader = req.getReader();
	    
	    while ((line = reader.readLine()) != null)
	    	sb.append(line);

		JsonParser parser = new JsonParser();

		return (JsonObject) parser.parse(sb.toString());
	}
	
	public static String getString(JsonObject data, String field, String otherwise){
		return (String) (data.has(field) ? data.get(field).getAsString() : otherwise);
	}
	
	public static int getInt(JsonObject data, String field, int otherwise){
		return (int) (data.has(field) ? data.get(field).getAsInt() : otherwise);
	}
	
	public static long getLong(JsonObject data, String field, long otherwise){
		return (long) (data.has(field) ? data.get(field).getAsLong() : otherwise);
	}
	
	public static boolean getBoolean(JsonObject data, String field, boolean otherwise){
		return (boolean) (data.has(field) ? data.get(field).getAsBoolean() : otherwise);
	}

	public static JsonArray getArray(JsonObject data, String field) {
		return data.has(field) ? data.get(field).getAsJsonArray() : null;
	}
	
	public static JsonObject getObject(JsonObject data, String field) {
		return data.has(field) ? data.get(field).getAsJsonObject() : null;
	}

	public static JsonObject parseRepresentation(Representation rep) {
		JsonParser parser = new JsonParser();
		try {
			return (JsonObject) parser.parse(rep.getText());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
}
