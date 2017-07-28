package magirator.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.JsonObject;

import magirator.data.entities.Player;
import magirator.data.entities.Result;
import magirator.data.entities.Tag;
import magirator.model.neo4j.LiveGames;
import magirator.model.neo4j.Tags;
import magirator.support.Constants;
import magirator.support.Json;

public class AddLiveGameTag extends ServerResource {

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
        	String text = request.get("text").getAsString();
        	String polarity = request.get("polarity").getAsString();
        	
        	int polarity_sign = 1;
    		if("negative".equals(polarity) || "-1".equals(polarity)){
    			polarity_sign = -1;
    		}
    		
        	create_tag: {
	    		Player tagger = LiveGames.getLivePlayer(token);
	    		
	    		if (tagger == null) {
	    			response.addProperty(Constants.result, "Could not find player.");
					break create_tag;
				}
	    		
	    		Result tagged = LiveGames.getPlayerResultInGame(token, live_id);
	    		
	    		if (tagged == null) {
	    			response.addProperty(Constants.result, "Could not find result.");
					break create_tag;
				}
	    		
	    		response.addProperty(Constants.result, "Could not create tag.");	    		
	    		Tag tag = new Tag(tagger.getId(), tagged.getId(), text, polarity_sign);
	    			    		
	    		response.addProperty(Constants.result, "Could not save tag.");
	    		
	    		
	        	if (Tags.addTagToEntity(tag)) {
	        		
	        		response.addProperty(Constants.result, "Could not retrive list of tags.");
					response.addProperty("tags", Tags.getEntityTagsAsJson(tag.getTagged()));
	        		
	        		response.addProperty(Constants.result, Constants.success);
				}
    		}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
