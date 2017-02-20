package magirator.micros;

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

import magirator.dataobjects.Participant;
import magirator.dataobjects.Player;
import magirator.model.neo4j.Games;
import magirator.support.Error;
import magirator.support.Variables;

/**
 * Servlet implementation class GetUnconfirmed
 */
@WebServlet("/GetUnconfirmed")
public class GetUnconfirmed extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetUnconfirmed --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Could not get unconfirmed Games, are you logged in?");
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {				
				ArrayList<Participant> participations = Games.getUnconfirmedGames(player.getId());
				
				result.addProperty("games", new Gson().toJson(participations));
				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to get unconfirmed Games, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetUnconfirmed -- Done");
	}

}
