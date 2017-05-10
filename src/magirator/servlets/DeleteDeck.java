package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import magirator.data.interfaces.IPlayer;
import magirator.model.neo4j.Decks;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Constants;

/**
 * Servlet implementation class DeleteDeck
 */
@WebServlet("/DeleteDeck")
public class DeleteDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- DeleteDeck --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not delete, are you logged in?");		
		
		JsonObject requestData = Json.parseRequestData(request);
		int deckId = Json.getInt(requestData, "id", 0);
		
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){

			result.addProperty(Constants.result, "Your deck should have been deleted, something must have gone wrong");	
			
			try {
				
				if (Decks.deleteDeck(deckId)){
					result.addProperty(Constants.result, Constants.success);
				}
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to delete deck, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- DeleteDeck -- Done");
	}
}
