package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import magirator.data.entities.Deck;
import magirator.data.interfaces.IPlayer;
import magirator.model.neo4j.Decks;
import magirator.support.Constants;
import magirator.support.Error;
import magirator.support.Json;

/**
 * Servlet implementation class GetDeckinLiveGame
 */
@WebServlet("/GetDeckinLiveGame")
public class GetDeckinLiveGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not get deck, are you logged in?");		
				
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				
				Deck deck = Decks.getPlayerDeckInLiveGame(player.getId());			
				result.addProperty("deck", new Gson().toJson(deck));				
				
				result.addProperty(Constants.result, Constants.success);
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to get deck, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());
	}

}
