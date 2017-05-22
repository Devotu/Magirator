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

import magirator.data.collections.PlayerDeck;
import magirator.data.entities.Deck;
import magirator.data.interfaces.IPlayer;
import magirator.model.neo4j.Decks;
import magirator.model.neo4j.Games;
import magirator.model.neo4j.IPlayers;
import magirator.support.Constants;
import magirator.support.Encryption;
import magirator.support.Error;
import magirator.support.Json;

/**
 * Servlet implementation class StartGame
 */
@WebServlet("/StartGame")
public class StartGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not start game, please log in first");
        
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Constants.result, "Something went wrong adding your game");
			
			try {
				JsonObject requestData = Json.parseRequestData(request);
				
				JsonArray requestParticipants = requestData.get("participants").getAsJsonArray();
				
				ArrayList<PlayerDeck> participants = new ArrayList<>();				
				
				for (JsonElement e : requestParticipants){
					
					JsonObject o = e.getAsJsonObject();
					
					IPlayer p = IPlayers.getIPlayer(o.get("playerId").getAsInt());
					Deck d = Decks.getDeck(o.get("deckId").getAsInt());
					
					participants.add( new PlayerDeck(p, d) );		
				}
				
				String token = Encryption.generateLiveToken();
				
				int gameId = Games.startGame(participants, player.getId(), token, Constants.startingLifeStandard);
				
				if (gameId > 0){
					
					result.addProperty("token", token);
					result.addProperty(Constants.result, Constants.success);
				}
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to start game, please login");
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());
	}

}
