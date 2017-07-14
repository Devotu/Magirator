package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import magirator.data.entities.Player;
import magirator.model.neo4j.LiveGames;
import magirator.support.Constants;
import magirator.support.Error;

/**
 * Servlet implementation class GetPlayerLiveGameToken
 */
@WebServlet("/GetPlayerLiveGameToken")
public class GetPlayerLiveGameToken extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not get live game token, are you logged in?");
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				String liveGameToken = LiveGames.getPlayerLiveGameToken(player.getId());				
				
				result.addProperty("token", liveGameToken);
				
				result.addProperty(Constants.result, Constants.success);
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to get game, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());
	}

}
