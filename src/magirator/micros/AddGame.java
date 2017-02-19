package magirator.micros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
import magirator.dataobjects.Play;
import magirator.dataobjects.Player;
import magirator.dataobjects.Result;
import magirator.model.neo4j.Decks;
import magirator.model.neo4j.Games;
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
				
				JsonArray requestResults = requestData.get("participants").getAsJsonArray();
				
				ArrayList<Result> results = new ArrayList<Result>();
				
				for (JsonElement e : requestResults){
					
					JsonObject o = e.getAsJsonObject();
					
					Deck deck = Decks.getDeck(o.get("deckId").getAsInt());
					Play play = new Play(o);
					
					results.add( new Result(deck, play) );
				}			
				
				
				if (Games.addGame(results)){
				
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
