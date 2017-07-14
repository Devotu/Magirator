package magirator.api.live;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;

import magirator.model.neo4j.LiveGames;
import magirator.support.Constants;
import magirator.support.Json;

public class GetGameId extends ServerResource {

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
        	
        	String player_token = request.get("token").getAsString();
        	
    		String id = LiveGames.getPlayerLiveId(player_token);
    		
    		if(!"".equals(id)){
    			
    			response.addProperty(Constants.result, Constants.success);
    			response.addProperty("id", id);
    		}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }

}
