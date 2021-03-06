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

import magirator.data.entities.Player;
import magirator.support.Error;
import magirator.support.Constants;

/**
 * Servlet implementation class GetPlayer
 */
@WebServlet("/GetPlayer")
public class GetPlayer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- GetPlayer --");

		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not fetch player, are you logged in?");
		
		try {	

			HttpSession session = request.getSession();
			Player player = (Player)session.getAttribute("player");
			
			if (player != null){
				result.addProperty(Constants.result, Constants.success);
				result.addProperty("player", new Gson().toJson(player));
				
			} else {
				
				result.addProperty(Constants.result, "Failed to get player, please login");
			}
		
		} catch (Exception e){
			response.getWriter().write( Error.printStackTrace(e) );
		}
		
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
        
		getServletContext().log("-- GetPlayer -- Done");
	}
}
