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

public class AlterLife extends ServerResource {

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
        	JsonArray life_updates = request.get("life_updates").getAsJsonArray();
        	
        	
        	for	(JsonElement element : life_updates){
        		JsonObject o = element.getAsJsonObject();
        		
            	if(LiveGames.changeLife(live_id, token, o.get("player_id").getAsInt(), o.get("new_life").getAsInt())){
            		response.addProperty(Constants.result, Constants.success);
            	} else {
            		response.addProperty(Constants.result, "Could not update life of " );
            	}
        		
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
