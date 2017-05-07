package magirator.restlets;

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
import magirator.data.interfaces.IPlayer;
import magirator.logic.Filter;
import magirator.model.neo4j.Games;
import magirator.model.neo4j.IPlayers;
import magirator.support.Json;
import magirator.support.Variables;
import magirator.view.Condition;

public class Graph extends ServerResource {

    @Post("json")
    public String toJson(Representation rep) {
    	
    	JsonObject response = new JsonObject();
    	response.addProperty(Variables.result, "Something went wrong");
		
        try {
        	
        	JsonObject request = Json.parseRepresentation(rep);
        	
        	if (request == null){
        		response.addProperty(Variables.result, "A problem occured parsing the input.");
        		return response.toString();
        	}
			
        	//extract player, graph and constrains
        	int playerId = request.get("playerId").getAsInt();
        	IPlayer player = IPlayers.getIPlayer(playerId);
        	
        	String graph = request.get("graph").getAsString();
        	JsonArray conditions = request.get("conditions").getAsJsonArray();
        	
        	
        	//get games
        	List<GameBundle> games = Games.getPlayerGames(playerId);        	
        	
        	//filter games
        	for (JsonElement e : conditions){
        		if (e.isJsonObject()){
        			
        			Condition condition = new Condition(e.getAsJsonObject());
        			
        			games = games.stream()
        					.filter(g -> Filter.gameHasCondition(g, condition))
        					.collect(Collectors.toList());
        		}        		
        	}
        	
        	//generate content
        	//magirator.grahp.Grapher.generateAxes();
        	//magirator.grahp.Grapher.generateBackground();
        	//magirator.grahp.Grapher.generateContent();
        	//...
        	
        	
        	response.addProperty("games", new Gson().toJson(games));
        	
        	response.addProperty("Player", player.getName());
        	response.addProperty("Graph", graph);
        	response.addProperty("Constrains", conditions.toString());
        	response.addProperty(Variables.result, "Success");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
