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

import magirator.data.interfaces.IPlayer;
import magirator.data.objects.Deck;
import magirator.data.objects.Minion;
import magirator.model.neo4j.Decks;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Validator;
import magirator.support.Variables;

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
		result.addProperty(Variables.result, "Could not add Minion Deck, please log in first");
        
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Variables.result, "Something went wrong adding the Minion");
			
			try {				
				JsonObject minionrequest = Json.parseRequestData(request);
				IPlayer minion = new Minion(minionrequest);
				Deck deck = new Deck(minionrequest);
							
				result.addProperty(Variables.result, "Deck must have a name and a format, Minon must be a valid Minion");
				
				if ( Validator.isRegisterdPlayer(minion) && Validator.isValidDeck(deck) ){
					
					if (Decks.addMinionDeck(minion, deck)){					
						result.addProperty("deck", new Gson().toJson(deck));
						result.addProperty(Variables.result, Variables.success);
					}
				}
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
		}

		response.setContentType("application/json");
		response.getWriter().write(result.toString());
		
		getServletContext().log("-- AddMinionDeck Done --");
	}
}
