package magirator.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import magirator.dataobjects.Deck;
import magirator.dataobjects.Play;
import magirator.dataobjects.Player;
import magirator.model.neo4j.Decks;
import magirator.model.neo4j.Games;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Variables;
import magirator.viewobjects.ListDeck;
import magirator.viewobjects.LoginCredentials;

/**
 * Servlet implementation class GetDeck
 */
@WebServlet("/GetDeck")
public class GetDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetDeck --");
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not get decks, are you logged in?");		
		
		JsonObject requestData = Json.parseRequestData(request);
		int deckId = Json.getInt(requestData, "id", 0);
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				
				Deck deck = Decks.getDeck(deckId);				
				result.addProperty("deck", new Gson().toJson(deck));				
				
				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to get deck, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetDeck -- Done");
	}

}
