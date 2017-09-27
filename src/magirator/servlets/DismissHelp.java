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

import magirator.data.collections.SettingsBundle;
import magirator.data.entities.Player;
import magirator.model.neo4j.Users;
import magirator.support.Constants;
import magirator.support.Error;
import magirator.support.Json;

/**
 * Servlet implementation class DismissHelp
 */
@WebServlet("/DismissHelp")
public class DismissHelp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- DismissHelp --");

		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not dismiss help, are you logged in?");
		
		try {	

			HttpSession session = request.getSession();
			Player player = (Player)session.getAttribute("player");
			
			if (player != null){
				
				JsonObject requestData = Json.parseRequestData(request);
				String helpId = Json.getString(requestData, "id", "");
				
				result.addProperty(Constants.result, "Failed to dismiss help");
				
				if(Users.dismissHelp(player, helpId)){
					result.addProperty(Constants.result, Constants.success);
				}				
				
			} else {
				
				result.addProperty(Constants.result, "Failed to dismiss help, please login");
			}
		
		} catch (Exception e){
			response.getWriter().write( Error.printStackTrace(e) );
		}
		
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
        
		getServletContext().log("-- DismissHelp -- Done");
	}
}
