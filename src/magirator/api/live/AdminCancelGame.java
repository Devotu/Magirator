package magirator.api.live;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import magirator.model.neo4j.LiveGames;
import magirator.support.Constants;
import magirator.support.Json;

public class AdminCancelGame extends ServerResource {

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

        	String live_id = request.get("live_id").getAsString();
        	String token = request.get("token").getAsString();
        	
        	if (LiveGames.isAdmin(token)) {
        		
        		response.addProperty(Constants.result, "Could not cancel game");
        		
        		if (LiveGames.cancelGame(live_id)) {
					
        			response.addProperty(Constants.result, Constants.success);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
