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

public class DeclareDead extends ServerResource {

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
        	int place = request.get("place").getAsInt();
        	
        	response.addProperty(Constants.result, "Could not declare " + token + " dead.");        	
        	int place_in_game = LiveGames.declareDead(live_id, token, place);
        	
        	if (place_in_game <= 2) { //Place is second => only winner is left
        		
        		wrap_up_game: {
        			
	        		response.addProperty(Constants.result, "Tried but could not declare winner in game " + live_id + ".");        		
	        		boolean winner_declared = LiveGames.declareWinner(live_id);
	        		
	        		if (!winner_declared) {
						break wrap_up_game;
					}
	        		
	        		response.addProperty(Constants.result, "Declared a winner in game " + live_id + "but could not end game.");
        			boolean game_ended = LiveGames.endGame(live_id);
        			
	        		if (!game_ended) {
						break wrap_up_game;
					}
	        		
	        		response.addProperty(Constants.result, Constants.success);
        		}
        		
			} else {
				
				response.addProperty(Constants.result, Constants.success);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
