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
import magirator.data.entities.Minion;
import magirator.data.interfaces.Player;
import magirator.model.neo4j.Decks;
import magirator.model.neo4j.Minions;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Validator;
import magirator.support.Constants;
import magirator.view.ListDeck;

/**
 * Servlet implementation class AddMinionDeck
 */
@WebServlet("/AddMinionDeck")
public class AddMinionDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		getServletContext().log("-- AddMinionDeck --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not add Minion Deck, please log in first");
        
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Constants.result, "Something went wrong adding the Minion");
			
			try {				
				JsonObject minionrequest = Json.parseRequestData(request);
				int minionId = Json.getInt(minionrequest, "id", 0);				
				Deck requestedDeck = new Deck(Json.getObject(minionrequest, "deck"));
				
				Minion minion = Minions.getMinion(minionId);
				
				result.addProperty(Constants.result, "Deck must have a name and a format, Minon must have a valid id");
				if (minion != null){
					
					if ( Validator.isRegisterdPlayer(minion) && Validator.isValidDeck(requestedDeck) ){
						
						Deck deck = Decks.addMinionDeck(minion, requestedDeck);
						
						if (deck != null){
							ListDeck ld = new ListDeck(deck, 0, 0);							
							result.addProperty("deck", new Gson().toJson(ld));
							result.addProperty(Constants.result, Constants.success);
						}
					}
				}
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
		}

		response.setContentType("application/json");
		response.getWriter().write(result.toString());
		
		getServletContext().log("-- AddMinionDeck Done --");
	}
}
