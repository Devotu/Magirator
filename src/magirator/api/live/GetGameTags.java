package magirator.api.live;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;

import magirator.data.entities.Result;
import magirator.model.neo4j.LiveGames;
import magirator.model.neo4j.Tags;
import magirator.support.Constants;
import magirator.support.Json;

//TODO redo into Tags Post/Get etc.
public class GetGameTags extends ServerResource {

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
        	
    		Result tagged = LiveGames.getPlayerResultInGame(token, live_id);
    		
    		if (tagged == null) {
    			response.addProperty(Constants.result, "Could not find result.");
    			return response.toString();
			}
    		
    		response.addProperty(Constants.result, "Could not retrive list of tags.");
			response.addProperty("tags", Tags.getEntityTagsAsJson(tagged.getId()));
    		
    		response.addProperty(Constants.result, Constants.success);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
