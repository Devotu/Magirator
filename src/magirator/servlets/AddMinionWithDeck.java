package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import magirator.dataobjects.Deck;
import magirator.dataobjects.Minion;
import magirator.model.neo4j.Players;
import magirator.model.neo4j.Users;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Validator;
import magirator.support.Variables;
import magirator.viewobjects.LoginCredentials;
import magirator.viewobjects.PublicPlayer;

/**
 * Servlet implementation class AddMinionWithDeck
 */
@WebServlet("/AddMinionWithDeck")
public class AddMinionWithDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- AddMinionWithDeck --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Something went wrong during singup, please try again");
		
		try {
			
			JsonObject minionrequest = Json.parseRequestData(request);
			Minion minion = new Minion(minionrequest);
			Deck deck = new Deck(minionrequest);
						
			result.addProperty(Variables.result, "Minion must have a name");
			
			if ( Validator.isValidMinion(minion) && Validator.isValidDeck(deck) ){
				//Add minion
				//Add deck
			}
			
			if(requestedUser.getPassword().length() > 7){
			
				result.addProperty(Variables.result, "Passwords does not match, please check and try again");
				
				if(requestedUser.getPassword().equals(requestedUser.getRetype())){
	
					result.addProperty(Variables.result, "Username already in use, please try another one");
					
					boolean userAvailable = Users.checkIfAvailable(requestedUser);
					
					if (userAvailable){
						
						PublicPlayer requestedPlayer = new PublicPlayer(signupRequest);
		
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
		
		getServletContext().log("-- AddMinionWithDeck Done --");
	}

}
