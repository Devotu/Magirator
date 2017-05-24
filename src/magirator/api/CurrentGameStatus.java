package magirator.api;

import java.util.List;
import java.util.stream.Collectors;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import magirator.data.collections.GameBundle;
import magirator.data.collections.PlayerStatus;
import magirator.data.collections.Participant;
import magirator.logic.Filter;
import magirator.logic.graph.Graph;
import magirator.logic.graph.Grapher;
import magirator.model.neo4j.Games;
import magirator.support.Constants;
import magirator.support.Json;
import magirator.view.Condition;

public class CurrentGameStatus extends ServerResource {

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
        	
        	List<PlayerStatus> status = Games.getGameStatus(token);
        	
        	Gson gson = new Gson();			
        	response.addProperty("status", new Gson().toJson(status));
        	
        	response.addProperty(Constants.result, "Success");			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }

}
