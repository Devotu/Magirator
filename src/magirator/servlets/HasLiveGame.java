package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import magirator.data.interfaces.IPlayer;
import magirator.model.neo4j.Games;
import magirator.support.Constants;
import magirator.support.Error;

/**
 * Servlet implementation class HasLiveGame
 */
@WebServlet("/HasLiveGame")
public class HasLiveGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			JsonObject result = new JsonObject();
			result.addProperty(Constants.result, "Could not get Games, are you logged in?");
			
			HttpSession session = request.getSession();
			IPlayer player = (IPlayer)session.getAttribute("player");
			
			//Player is logged in
			if (player != null){
				
				try {									
					String live = Games.getPlayerLiveGameToken(player.getId());
					result.addProperty("live", live);
									
					result.addProperty(Constants.result, Constants.success);
					
				} catch (Exception e) {
					result.addProperty(Constants.result, Error.printStackTrace(e));
				}
				
			} else {
				
				result.addProperty(Constants.result, "Failed to get Games, please login");
			}
			
			response.setContentType("application/json");
			response.getWriter().write(result.toString());
	}

}
