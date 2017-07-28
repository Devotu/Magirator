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

public class RemoveTag extends ServerResource {

	/**
	 * Removed the tag from the current result and deletes it only if no other entity has it as tag
	 */
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

        	int tag_id = request.get("tag_id").getAsInt();
        	String live_id = request.get("live_id").getAsString();
        	String token = request.get("token").getAsString();
        	
        	response.addProperty(Constants.result, "Could not retrive related result.");
        	Result result = LiveGames.getPlayerResultInGame(token, live_id);
        	
        	response.addProperty(Constants.result, "Could not remove tag.");
        	int remainingUses = Tags.removeTagFromEntity(tag_id, result.getId()); 
        	
        	if (remainingUses == 0) {
				Tags.deleteTag(tag_id);
			}
    		
    		response.addProperty(Constants.result, Constants.success);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
