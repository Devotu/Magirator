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
import magirator.support.Json;

/**
 * Servlet implementation class UpdateLivePlayerLife
 */
@WebServlet("/UpdateLivePlayerLife")
public class UpdateLivePlayerLife extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not update life, please log in first");
        
		HttpSession session = request.getSession();
		IPlayer player = (IPlayer)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Constants.result, "Something went wrong updating life");
			
			JsonObject requestData = Json.parseRequestData(request);
			
			int playerId = Json.getInt(requestData, "id", 0);
			int life = Json.getInt(requestData, "life", 0);
			long time = Json.getLong(requestData, "time", 0);
			
			try {
				
				if (playerId != 0 && time != 0) {
					if(Games.updateLivePlayerLife(playerId, player.getId(), life, time)){
						result.addProperty(Constants.result, Constants.success);
					}					
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
