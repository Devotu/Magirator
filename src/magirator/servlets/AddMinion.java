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

import magirator.data.entities.Minion;
import magirator.data.entities.User;
import magirator.data.interfaces.Player;
import magirator.logic.Ranker;
import magirator.model.neo4j.Minions;
import magirator.model.neo4j.Users;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Lists;
import magirator.support.Validator;
import magirator.support.Constants;
import magirator.view.Opponent;

/**
 * Servlet implementation class AddMinion
 */
@WebServlet("/AddMinion")
public class AddMinion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		getServletContext().log("-- AddMinion --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not add Minion, please log in first");
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		if (player != null){

			result.addProperty(Constants.result, "Something went wrong adding the Minion");
			
			try {
				
				JsonObject minionrequest = Json.parseRequestData(request);
				Player requestedMinion = new Minion(minionrequest);
							
				result.addProperty(Constants.result, "Minion must have a name");
				
				if ( Validator.isValidPlayer(requestedMinion) ){
					
					result.addProperty(Constants.result, "Could not get user. Adding Minion failed");
					
					User user = Users.getUser(player);
					
					if (user != null){
						
						result.addProperty(Constants.result, "Something went wrong adding your Minion");
						
						Minion minion = Minions.addMinion(user, requestedMinion);
						Opponent opponent = Ranker.rankMinion(minion);
						
						if ( minion != null ){
							result.addProperty("minion", new Gson().toJson(minion));
							result.addProperty("opponent", new Gson().toJson(opponent));
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
		
		getServletContext().log("-- AddMinion Done --");
	}

}
