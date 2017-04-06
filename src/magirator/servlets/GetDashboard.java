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

import magirator.dataobjects.Participant;
import magirator.interfaces.IPlayer;
import magirator.model.neo4j.Games;
import magirator.support.Error;
import magirator.support.Variables;

/**
 * Servlet implementation class GetDashboard
 */
@WebServlet("/GetDashboard")
public class GetDashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- Dashboard --");

		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not fetch current information");
		
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer) session.getAttribute("player");

		// Player is logged in
		if (player != null) {
			try {
				ArrayList<Participant> participations = Games.getUnconfirmedGames(player.getId());
				result.addProperty("unconfirmed", participations.size());
				
				result.addProperty("player", new Gson().toJson(player));

				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				response.getWriter().write(Error.printStackTrace(e));
			}

		} else {

			result.addProperty(Variables.result, "Failed to confirm game, please login");
		}

		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- Dashboard -- Done");
	}

}
