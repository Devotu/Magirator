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
        	
        	String playerName = request.get("player_name").getAsString();
        	int playerId = request.get("player_id").getAsInt();
        	String deckName = request.get("deck_name").getAsString();
        	int deckId = request.get("deck_id").getAsInt();
        	
        	
        	//Does player exist
        	//If not create anonymous player
        	
        	response.addProperty(Constants.result, "You are already in a game.");
        	
        	if(!LiveGames.isPlayerInGame(playerId)){
        		
        		response.addProperty(Constants.result, "A problem occured starting the game.");
        		
        		String token = LiveGames.startNewGame(deckId);
        		
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
