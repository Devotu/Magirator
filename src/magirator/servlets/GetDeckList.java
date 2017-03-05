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
import magirator.dataobjects.Result;
import magirator.model.neo4j.Decks;
import magirator.model.neo4j.Games;
import magirator.support.Error;
import magirator.support.Variables;
import magirator.viewobjects.ListDeck;

/**
 * Servlet implementation class GetDeckList
 */
@WebServlet("/GetDeckList")
public class GetDeckList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		getServletContext().log("-- GetDeckList --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Could not get deck list, are you logged in?");
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				ArrayList<Deck> decks = Decks.getPlayerDecks(player);
				
				//There are decks
				if (decks != null && decks.size() > 0){
					
					ArrayList<ListDeck> listDecks = new ArrayList<ListDeck>();
					
					for	(Deck d : decks){
						
						ArrayList<Result> results = Games.getDeckPlayed(d);
						
						float wins = 0;
						float games = 0;
						
						for (Result r : results){
							
							if (r.getConfirmed()){
								
								games++;
								
								if (r.getPlace() == 1){ //Win
									wins++;
								}
							}
						}
						
						int winrate = 0;
						
						if (games > 0) {
							winrate = Math.round((wins / games) * 100);
						}
						
						listDecks.add(new ListDeck(d, winrate, (int)games));					
					}
					
					result.addProperty("decks", new Gson().toJson(listDecks));
				}
				
				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to get deck list, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetDeckList -- Done");
	}

}
