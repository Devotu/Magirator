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
import magirator.data.entities.User;
import magirator.logic.LoginCredentials;
import magirator.model.neo4j.Players;
import magirator.model.neo4j.Users;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Constants;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- Login --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Username or password is incorrect");
		
		JsonObject credentials;
		try {
			credentials = Json.parseRequestData(request);
			
			LoginCredentials loginCredentials = new LoginCredentials(credentials);
			
			if (loginCredentials.isValidLogin()) {
				loginCredentials.encryptPassword();
				User user = Users.login(loginCredentials);
				if (user != null) { //Inloggningen gick bra

					result.addProperty(Constants.result, "Could not find player");

					Player player = Players.getPlayer(user);

					if (player != null) {

						HttpSession session = request.getSession();
						session.setAttribute("player", player);
						result.addProperty(Constants.result, Constants.success);

						Users.clearReset(loginCredentials);
					}
				} 
			}
			
		} catch (Exception e) {
			result.addProperty(Constants.result, Error.printStackTrace(e));
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- Login -- Done");
	}

}
