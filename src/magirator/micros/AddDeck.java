package magirator.micros;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import magirator.dataobjects.Deck;
import magirator.dataobjects.Player;
import magirator.model.neo4j.Decks;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Variables;

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
		result.addProperty(Variables.result, "Something went wrong adding your deck");
		
		JsonObject data = Json.parseRequestData(request);
		
		Player player = new Player( data.get("player").getAsJsonObject() );
		Deck deck = new Deck( data.get("deck").getAsJsonObject() );
		
		try	{
			result.addProperty(Variables.result, Decks.addDeck(player, deck) );			
		} catch (Exception e) {
			result.addProperty(Variables.result, Error.printStackTrace(e));
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());

		getServletContext().log("-- AddDeck -- Done");
	}

}
