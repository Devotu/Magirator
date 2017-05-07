package magirator.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import magirator.data.collections.IPlayerGame;
import magirator.data.entities.Minion;
import magirator.data.entities.User;
import magirator.data.interfaces.IPlayer;
import magirator.logic.Ranker;
import magirator.model.neo4j.IPlayers;
import magirator.model.neo4j.Minions;
import magirator.model.neo4j.Users;
import magirator.support.Error;
import magirator.support.Lists;
import magirator.support.Variables;
import magirator.view.Opponent;

/**
 * Servlet implementation class GetOpponents
 */
@WebServlet("/GetOpponents")
public class GetOpponents extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetOpponents --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Could not get opponents, are you logged in?");
		
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			List<Opponent> opponents = new ArrayList<>();
			
			try { //Get potential opponents, rank them, Merge into list
				
				List<IPlayerGame> previous = IPlayers.getPlayerPreviousOpponents(player);
				List<Opponent> previousOpponents = Ranker.rankPrevious(previous);
				Lists.mergeToOpponentList(opponents, previousOpponents);
				
				User user = Users.getUser(player);
				List<Minion> minions = Minions.getUserMinions(user);
				List<Opponent> minionOpponents = Ranker.rankMinions(minions);
				Lists.mergeToOpponentList(opponents, minionOpponents);
				
				Lists.orderOpponents(opponents);
				
				result.addProperty("opponents", new Gson().toJson(opponents));
				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to get opponents, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetOpponents -- Done");
	}

}
