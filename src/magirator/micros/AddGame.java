package magirator.micros;

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

import magirator.dataobjects.Deck;
import magirator.dataobjects.Player;
import magirator.dataobjects.Result;
import magirator.dataobjects.Participant;
import magirator.model.neo4j.Decks;
import magirator.model.neo4j.Games;
import magirator.model.neo4j.Players;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Variables;

/**
 * Servlet implementation class AddGame
 */
@WebServlet("/AddGame")
public class AddGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- AddGame --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Could not add game, please log in first");
        
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Variables.result, "Something went wrong adding your game");
			
			try {
				JsonObject requestData = Json.parseRequestData(request);		
				
				JsonArray requestParticipants = requestData.get("participants").getAsJsonArray();
				
				ArrayList<Participant> participants = new ArrayList<Participant>();
				int initiatorId = 0;
				
				for (JsonElement e : requestParticipants){
					
					JsonObject o = e.getAsJsonObject();
					
					Player p = Players.getPlayer(o.get("playerId").getAsInt());
					Deck d = Decks.getDeck(o.get("deckId").getAsInt());
					Result r = new Result(o);
					
					participants.add( new Participant(p, d, r, null) );
					
					if(p.getId() == player.getId()){
						
					}
				}
				
				boolean draw = requestData.get("draw").getAsBoolean();		
				
				if (Games.addGame(participants, draw, initiatorId)){
				
					result.addProperty(Variables.result, Variables.success);					
				}
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to add game, please login");
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());

		getServletContext().log("-- AddGame -- Done");
	}

}
