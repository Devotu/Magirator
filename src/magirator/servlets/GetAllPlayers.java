package magirator.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import magirator.data.entities.Player;
import magirator.model.neo4j.Players;
import magirator.support.Error;
import magirator.support.Variables;

/**
 * Servlet implementation class GetAllPlayers
 */
@WebServlet("/GetAllPlayers")
public class GetAllPlayers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetAllPlayers --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Could not get players, are you logged in?");
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		if (player != null){
			
			try { //Get potential opponents, rank them, Merge into list
				
				List<Player> players = Players.getAllPlayers(player);
				
				result.addProperty("players", new Gson().toJson(players));
				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to get players, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetAllPlayers -- Done");
	}

}
