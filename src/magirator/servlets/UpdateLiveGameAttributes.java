package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import magirator.data.interfaces.Player;
import magirator.model.neo4j.Games;
import magirator.support.Constants;
import magirator.support.Error;
import magirator.support.Json;

/**
 * Servlet implementation class UpdateLiveGameAttributes
 */
@WebServlet("/UpdateLiveGameAttributes")
public class UpdateLiveGameAttributes extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not update game, please log in first");
        
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Constants.result, "Something went wrong updating your game");
			
			JsonObject requestData = Json.parseRequestData(request);
			
			boolean draw = Json.getBoolean(requestData, "draw", false);
			
			try {
				
				if (Games.updateLiveGameAttributes(player.getId(), draw)) {
					result.addProperty(Constants.result, Constants.success);
				}
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to confirm game, please login");
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());
	}

}
