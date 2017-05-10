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
import magirator.logic.Filter;
import magirator.logic.graph.Graph;
import magirator.logic.graph.Grapher;
import magirator.model.neo4j.Games;
import magirator.support.Json;
import magirator.support.Constants;
import magirator.view.Condition;

public class GenerateGraph extends ServerResource {

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
			
        	//extract player, graph and constrains
        	int playerId = request.get("playerId").getAsInt();
        	
        	String targetGraph = request.get("graph").getAsString();
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
        	Grapher grapher = new Grapher("Graph", games);
        	Graph graph = null;
        	
        	if ("winrate".equals(targetGraph)) {
        		
        		graph = grapher.generateWinrateGraph();
        		
			} else if ("colorbars".equals(targetGraph)){
				
				graph = grapher.generateColorbarsGraph();
			}
        	
        	//generate output
        	Gson gson = new Gson();
        	response.add("graph", (JsonObject) gson.toJsonTree(graph));
        	
        	response.addProperty(Constants.result, "Success");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return response.toString();
    }
}
