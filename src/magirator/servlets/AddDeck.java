package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import magirator.data.entities.Deck;
import magirator.data.entities.Player;
import magirator.model.neo4j.Decks;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Constants;

/**
 * Servlet implementation class AddDeck
 */
@WebServlet(description = "Add a new deck to the current user", urlPatterns = { "/AddDeck" })
public class AddDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- AddDeck --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not add deck, please log in first");
        
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Constants.result, "Something went wrong adding your deck");
			
			try {
				JsonObject requestData = Json.parseRequestData(request);		
				Deck deck = new Deck( requestData.get("deck").getAsJsonObject() );
				
				if (Decks.addDeck(player, deck)){
				
					result.addProperty(Constants.result, Constants.success);					
				}
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to add deck, please login");
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());

		getServletContext().log("-- AddDeck -- Done");
	}

}
