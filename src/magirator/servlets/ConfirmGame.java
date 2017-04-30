package magirator.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import magirator.data.interfaces.IPlayer;
import magirator.data.objects.Rating;
import magirator.data.objects.Tag;
import magirator.model.neo4j.Games;
import magirator.model.neo4j.Tags;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Variables;

/**
 * Servlet implementation class ConfirmGame
 */
@WebServlet("/ConfirmGame")
public class ConfirmGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- ConfirmGame --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Could not confirm game, please log in first");
        
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Variables.result, "Something went wrong adding your game");
			
			JsonObject requestData = Json.parseRequestData(request);
			int gameId = Json.getInt(requestData, "gameId", 0);
			int playId = Json.getInt(requestData, "id", 0);
			boolean confirm = Json.getBoolean(requestData, "confirm", false);
			String comment = Json.getString(requestData, "comment", "No comment");		
			
			Rating rating = new Rating(Json.getObject(requestData, "rating"));
			
			JsonArray tag_array = Json.getArray(requestData, "tags");
			ArrayList<Tag> tags = new ArrayList<Tag>();
			
			for (JsonElement t : tag_array){
				JsonObject tag = t.getAsJsonObject();
				tags.add(new Tag( player.getId(), tag.get("tagged").getAsInt(), tag.get("tag").getAsString(), tag.get("polarity").getAsInt() ));
			}
			
			try {				
				
				if (Games.confirmGame(playId, confirm, comment, rating)){
					
					result.addProperty(Variables.result, "Confirmed game but something went wrong with the tags");	
					
					if(Tags.addTagsToResultsInGame(tags, gameId)){
						result.addProperty(Variables.result, Variables.success);
					}				
				}
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to confirm game, please login");
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());

		getServletContext().log("-- ConfirmGame -- Done");
	}

}
