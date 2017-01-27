package magirator.support;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Json {

	public static JsonObject parseRequestData(HttpServletRequest req) throws IOException{
		
		StringBuffer sb = new StringBuffer();
		String line = null;
		
	    BufferedReader reader = req.getReader();
	    
	    while ((line = reader.readLine()) != null)
	    	sb.append(line);

		JsonObject data = null;
		
		JsonParser parser = new JsonParser();

		return (JsonObject) parser.parse(sb.toString());
	}
	
	public static String getString(JsonObject data, String field, String otherwise){
		return (String) (data.has(field) ? data.get(field).getAsString() : otherwise);
	}
}
