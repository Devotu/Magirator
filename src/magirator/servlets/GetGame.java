package magirator.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import magirator.data.collections.PlayerGameResult;
import magirator.data.entities.Tag;
import magirator.data.entities.Player;
import magirator.model.neo4j.Games;
import magirator.model.neo4j.Tags;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Constants;

/**
 * Servlet implementation class GetGame
 */
@WebServlet("/GetGame")
public class GetGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetGame --");
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not get game, are you logged in?");		
		
		JsonObject requestData = Json.parseRequestData(request);
		int gameId = Json.getInt(requestData, "id", 0);
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				ArrayList<PlayerGameResult> participants = Games.getParticipants(gameId);
				List<Tag> taglist = Tags.getTagsInGame(gameId);
				
				for	(PlayerGameResult p : participants){
					for (Tag t : taglist){
						if(t.getTagged() == p.getResult().getId()){
							p.addTag(t);
						}
					}
					
					if(p.getPlayer().getId() != player.getId()){
						p.setRating(null);
					}
				}
				
				result.addProperty("participants", new Gson().toJson(participants));
				result.addProperty(Constants.result, Constants.success);
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to get game, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetGame -- Done");
	}

}
