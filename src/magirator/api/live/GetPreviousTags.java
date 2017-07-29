package magirator.api.live;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;

import magirator.data.entities.Player;
import magirator.model.neo4j.LiveGames;
import magirator.model.neo4j.Tags;
import magirator.support.Constants;
import magirator.support.Json;

public class GetPreviousTags extends ServerResource {

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

        	String token = request.get("token").getAsString();
        	
        	Player tagger = LiveGames.getLivePlayer(token);
    		
    		if (tagger == null) {
    			response.addProperty(Constants.result, "Could not find player.");
    			return response.toString();
			}
    		
    		response.addProperty(Constants.result, "Could not retrive list of tags.");
			response.addProperty("tags", Tags.getTaggerTagsAsJson(tagger.getId()));
    		
    		response.addProperty(Constants.result, Constants.success);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
