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

import magirator.data.collections.Participant;
import magirator.data.collections.PlayerDeck;
import magirator.data.entities.Tag;
import magirator.data.interfaces.IPlayer;
import magirator.model.neo4j.Games;
import magirator.model.neo4j.Tags;
import magirator.support.Constants;
import magirator.support.Error;
import magirator.support.Json;

/**
 * Servlet implementation class GetLiveGame
 */
@WebServlet("/GetLiveGame")
public class GetLiveGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not get live game, are you logged in?");
		
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				List<PlayerDeck> participants = Games.getPlayerLiveGame(player.getId());
				
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
	}

}
