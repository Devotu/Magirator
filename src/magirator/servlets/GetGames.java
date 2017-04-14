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

import magirator.data.collections.Participant;
import magirator.data.interfaces.IPlayer;
import magirator.model.neo4j.Games;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Variables;

/**
 * Servlet implementation class GetGames
 */
@WebServlet("/GetGames")
public class GetGames extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetGames --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Could not get Games, are you logged in?");
		
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				JsonObject requestData = Json.parseRequestData(request);
				int deckId = Json.getInt(requestData, "id", 0);
				
				ArrayList<Participant> participations = Games.getDeckParticipations(deckId);
				
				result.addProperty("games", new Gson().toJson(participations));
				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to get Games, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetGames -- Done");
	}

}
