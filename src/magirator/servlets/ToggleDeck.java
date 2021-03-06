package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import magirator.data.entities.Player;
import magirator.model.neo4j.Decks;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Constants;

/**
 * Servlet implementation class ToggleDeck
 */
@WebServlet("/ToggleDeck")
public class ToggleDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- ToggleDeck --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not toggle, are you logged in?");		
		
		JsonObject requestData = Json.parseRequestData(request);
		int deckId = Json.getInt(requestData, "id", 0);
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){

			result.addProperty(Constants.result, "Your deck should have been toggled, something must have gone wrong");	
			
			try {
				
				if (Decks.toggleDeck(deckId)){
					result.addProperty(Constants.result, Constants.success);
				}
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to get deck, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- ToggleDeck -- Done");
	}

}
