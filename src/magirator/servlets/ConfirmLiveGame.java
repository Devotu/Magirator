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

import magirator.data.entities.Rating;
import magirator.data.entities.Tag;
import magirator.data.interfaces.Player;
import magirator.model.neo4j.Games;
import magirator.model.neo4j.Minions;
import magirator.model.neo4j.Tags;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Constants;

/**
 * @deprecated
 * Servlet implementation class ConfirmGame
 */
@WebServlet("/ConfirmLiveGame")
public class ConfirmLiveGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not confirm game, please log in first");
        
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Constants.result, "Could not get id of player");
			
			JsonObject requestData = Json.parseRequestData(request);
			
			int playerId = Json.getInt(requestData, "playerId", 0);
			
			try {
				if (playerId != 0 && (playerId == player.getId() || Minions.isMinionOfPlayer(playerId, player.getId()) ) ) {
					
					String comment = Json.getString(requestData, "comment", "No comment");
					
					Rating rating = null;
					JsonObject ratingJson = Json.getObject(requestData, "rating");
					
					if (ratingJson != null) {
						rating = new Rating(ratingJson);
					}
					
					JsonArray tag_array = Json.getArray(requestData, "tags");
					ArrayList<Tag> tags = new ArrayList<Tag>();
					if (tag_array != null) {
						for (JsonElement t : tag_array) {
							JsonObject tag = t.getAsJsonObject();
							tags.add(new Tag(player.getId(), player.getId(), tag.get("tag").getAsString(),
									tag.get("polarity").getAsInt()));
						}
					}

					int gameId = Games.getPlayerLiveGameId(player.getId());
					boolean isDraw = Games.gameIsDraw(gameId);

					int place = 0;
					if (!isDraw) {
						place = Games.getPlaceInGame(gameId);
					}
					
					int confirmedGame = Games.confirmLiveGame(playerId, place, comment, rating);
					if (confirmedGame != 0) {
						result.addProperty(Constants.result, "Confirmed game but something went wrong with the tags");
	
						if (Tags.addTagsToResultsInGame(tags, confirmedGame)) {
							result.addProperty(Constants.result, Constants.success);
						}
					}

					if (Games.endLiveGame(player.getId())) {
						result.addProperty("End", "All players have confirmed the game and it is now closed");
					} else {
						result.addProperty("End",
								"Your participation has been confirmed but one or more players have not confirmed the game. The game will be ended as soon as they confirm the game.");
					}
					
					if(Games.placeInGame(playerId, gameId) == 1){
						result.addProperty("win", true);
					}
				}
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to confirm game, please login");
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());
	}

}
