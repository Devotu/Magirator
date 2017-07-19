package magirator.api.live;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;

import magirator.model.neo4j.LiveGames;
import magirator.support.Constants;
import magirator.support.Json;

public class GameStatus extends ServerResource {

    @Post("json")
    public String toJson(Representation rep) {
    	
    	JsonObject response = new JsonObject();
    	response.addProperty(Constants.result, "Something went wrong");
		
        try {
        	
        	JsonObject request = Json.parseRepresentation(rep);
        	
        	if (request == null){
        		response.addProperty(Constants.result, "A problem occured parsing the input.");
        		return response.toString();
        	}        	
        	
        	String liveId = request.get("live_id").getAsString();
        	        	
        	response.addProperty(Constants.result, "You are already in a game.");
        	
        	
        	response.addProperty("status", LiveGames.getGameStatusAsJson(liveId));        	
        	response.addProperty(Constants.result, Constants.success);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
