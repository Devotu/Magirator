package magirator.micros;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import magirator.dataobjects.Player;
import magirator.model.neo4j.Games;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Variables;

/**
 * Servlet implementation class ConfirmGame
 */
@WebServlet("/ConfirmGame")
public class ConfirmGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- ConfirmGame --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Could not confirm game, please log in first");
        
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			result.addProperty(Variables.result, "Something went wrong adding your game");
			
			JsonObject requestData = Json.parseRequestData(request);
			int playId = Json.getInt(requestData, "id", 0);
			boolean confirm = Json.getBoolean(requestData, "id", false);
			String comment = Json.getString(requestData, "id", "No comment");
			
			try {				
				
				if (Games.confirmGame(playId, confirm, comment)){
				
					result.addProperty(Variables.result, Variables.success);					
				}
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to confirm game, please login");
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());

		getServletContext().log("-- ConfirmGame -- Done");
	}

}
