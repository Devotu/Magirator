package magirator.api.live;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;

import magirator.model.neo4j.LiveGames;
import magirator.support.Constants;
import magirator.support.Json;

public class JoinGame extends ServerResource {

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
        	
        	int playerId = request.get("player_id").getAsInt();
        	int deckId = request.get("deck_id").getAsInt();
        	String live_id = request.get("live_id").getAsString();
        	
        	
        	//Does player exist
        	//If not create anonymous player
        	
        	response.addProperty(Constants.result, "You are already in a game.");
        	
        	if(!LiveGames.isPlayerInGame(playerId)){
        		
        		response.addProperty(Constants.result, "A problem occured joining the game.");
        		
        		String token = LiveGames.joinGame(deckId, live_id);
        		
        		if(!"".equals(token)){
        			
        			response.addProperty(Constants.result, Constants.success);
        			response.addProperty("token", token);
        		}
        	}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
