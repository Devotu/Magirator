package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

import magirator.logic.LoginCredentials;
import magirator.model.neo4j.Players;
import magirator.model.neo4j.Users;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Variables;
import magirator.view.PlayerName;

/**
 * Servlet implementation class Signup
 */
@WebServlet("/Signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		getServletContext().log("-- Signup --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Something went wrong during singup, please try again");
		
		try {
			
			JsonObject signupRequest = Json.parseRequestData(request);
			LoginCredentials requestedUser = new LoginCredentials(signupRequest);
			
			result.addProperty(Variables.result, "Password length is to short, please choose a password at least 8 characters long");
			
			if(requestedUser.getPassword().length() > 7){
			
				result.addProperty(Variables.result, "Passwords does not match, please check and try again");
				
				if(requestedUser.getPassword().equals(requestedUser.getRetype())){
	
					result.addProperty(Variables.result, "Username already in use, please try another one");
					
					boolean userAvailable = Users.checkIfAvailable(requestedUser);
					
					if (userAvailable){
						
						PlayerName requestedPlayer = new PlayerName(signupRequest);
		
						result.addProperty(Variables.result, "Playername already in use, please try another one");
						
						boolean playerAvailable = Players.checkIfAvailable(requestedPlayer);
						
						if (playerAvailable){
							
							result.addProperty(Variables.result, "This user and player name really should be ok, please try again");
							
							requestedUser.encryptPassword();
							
							boolean signupSuccessful = Users.signup(requestedUser, requestedPlayer);
							
							if (signupSuccessful){
								
								result.addProperty(Variables.result, Variables.success);
							}					
						}
					}
				}
			}
		} catch (Exception e) {
			result.addProperty(Variables.result, Error.printStackTrace(e));
		}

		response.setContentType("application/json");
		response.getWriter().write(result.toString());
		
		getServletContext().log("-- Signup Done --");
	}

}
