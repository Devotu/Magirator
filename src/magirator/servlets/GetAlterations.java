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

import magirator.data.collections.Alteration;
import magirator.data.entities.Player;
import magirator.data.interfaces.Player;
import magirator.model.neo4j.Decks;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Constants;

/**
 * Servlet implementation class GetAlterations
 */
@WebServlet("/GetAlterations")
public class GetAlterations extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetAlterations --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Could not get Alterations, are you logged in?");
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				JsonObject requestData = Json.parseRequestData(request);
				int deckId = Json.getInt(requestData, "id", 0);
				
				ArrayList<Alteration> alterations = Decks.getDeckAlterations(deckId);
				
				result.addProperty("alterations", new Gson().toJson(alterations));
				result.addProperty(Constants.result, Constants.success);
				
			} catch (Exception e) {
				result.addProperty(Constants.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Constants.result, "Failed to get Alterations, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetAlterations -- Done");
	}

}
