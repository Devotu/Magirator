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

import magirator.data.collections.Participant;
import magirator.data.interfaces.IPlayer;
import magirator.data.objects.Deck;
import magirator.data.objects.Result;
import magirator.data.objects.Tag;
import magirator.model.neo4j.Decks;
import magirator.model.neo4j.Games;
import magirator.model.neo4j.IPlayers;
import magirator.model.neo4j.Tags;
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
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Variables.result, "Something went wrong adding your game");
			
			try {
				JsonObject requestData = Json.parseRequestData(request);		
				
				JsonArray requestParticipants = requestData.get("participants").getAsJsonArray();
				
				ArrayList<Participant> participants = new ArrayList<Participant>();
				ArrayList<Tag> tags = new ArrayList<Tag>();
				
				for (JsonElement e : requestParticipants){
					
					JsonObject o = e.getAsJsonObject();
					
					IPlayer p = IPlayers.getIPlayer(o.get("playerId").getAsInt());
					Deck d = Decks.getDeck(o.get("deckId").getAsInt());
					Result r = new Result(o);
					
					participants.add( new Participant(p, d, r, null) );
					
					JsonArray tag_array = o.get("tags").getAsJsonArray();
					
					for (JsonElement t : tag_array){
						JsonObject tag = t.getAsJsonObject();
						tags.add(new Tag( player.getId(), p.getId(), tag.get("tag").getAsString(), tag.get("polarity").getAsInt() ));
					}
					
				}
				
				boolean draw = requestData.get("draw").getAsBoolean();		
				
				int gameId = Games.addGame(participants, draw, player.getId());
				
				if (gameId > 0){
				
					result.addProperty(Variables.result, "Game added but something went wrong tagging the results");
					
					if(Tags.addTagsToResultsInGame(tags, gameId)){
						result.addProperty(Variables.result, Variables.success);
					}
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
