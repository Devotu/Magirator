package magirator.micros;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import magirator.dataobjects.Alteration;
import magirator.dataobjects.Deck;
import magirator.dataobjects.Player;
import magirator.model.neo4j.Decks;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Variables;

/**
 * Servlet implementation class GetAlteration
 */
@WebServlet("/GetAlteration")
public class GetAlteration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetAlteration --");
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not get alteration, are you logged in?");		
		
		JsonObject requestData = Json.parseRequestData(request);
		int alterationId = Json.getInt(requestData, "id", 0);
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				Alteration alteration = Decks.getAlteration(alterationId);
						
				result.addProperty("alteration", new Gson().toJson(alteration));				
				
				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to get alteration, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetAlteration -- Done");
	}

}
