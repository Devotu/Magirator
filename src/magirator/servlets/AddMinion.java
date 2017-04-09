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

import magirator.dataobjects.Minion;
import magirator.dataobjects.User;
import magirator.interfaces.IPlayer;
import magirator.model.neo4j.Minions;
import magirator.model.neo4j.Users;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Validator;
import magirator.support.Variables;

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
		result.addProperty(Variables.result, "Could not add Minion, please log in first");
		
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		if (player != null){

			result.addProperty(Variables.result, "Something went wrong adding the Minion");
			
			try {
				
				JsonObject minionrequest = Json.parseRequestData(request);
				Minion requestedMinion = new Minion(minionrequest);
							
				result.addProperty(Variables.result, "Minion must have a name");
				
				if ( Validator.isValidMinion(requestedMinion) ){
					
					result.addProperty(Variables.result, "Could not get user. Adding Minion failed");
					
					User user = Users.getUser(player);
					
					if (user != null){
						
						result.addProperty(Variables.result, "Something went wrong adding your Minion");
						
						Minion minion = Minions.addMinion(user, requestedMinion);
						
						if ( minion != null ){
							result.addProperty("minion", new Gson().toJson(requestedMinion));
							result.addProperty(Variables.result, Variables.success);
						}
					}
				}
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
		}

		response.setContentType("application/json");
		response.getWriter().write(result.toString());
		
		getServletContext().log("-- AddMinion Done --");
	}

}
