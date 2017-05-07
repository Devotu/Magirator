package magirator.servlets;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

import magirator.data.entities.Reset;
import magirator.logic.LoginCredentials;
import magirator.model.neo4j.Users;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Validator;
import magirator.support.Variables;

/**
 * Servlet implementation class SetNewPassword
 */
@WebServlet("/SetNewPassword")
public class SetNewPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		getServletContext().log("-- Setting new password --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Variables.result, "Reset code, username or password is incorrect");
		
		JsonObject credentials;
		
		try {
			credentials = Json.parseRequestData(request);
			
			LoginCredentials loginCredentials = new LoginCredentials(credentials);
						
			if (Validator.hasValidResetCredentials(loginCredentials)) {
				
				Reset reset = Users.getUserReset(loginCredentials);				
				
				reseting:{
					
					if (reset == null){
						result.addProperty(Variables.result, "No reset request found for user " + loginCredentials.getUsername() + ".");
						break reseting;
					}
					
					if (! reset.getCode().equals( loginCredentials.getCode() ) ){
						result.addProperty(Variables.result, "Submitted reset code does not match registerd code. Please check spelling.");
						break reseting;
					}
					
					int validMinutes = 30;
					Calendar then = Calendar.getInstance();
					then.add(Calendar.MINUTE, -validMinutes);
					
					if ( then.before( reset.getCreated() ) ){
						result.addProperty(Variables.result, "Reset has expired (valid for " + validMinutes + " minutes. Try again.");
						break reseting;
					}
					
					if (! Users.clearReset(loginCredentials)){
						result.addProperty(Variables.result, "Something went wrong removing the reset. Try again");
						break reseting;
					}

					loginCredentials.encryptPassword();
					
					if (! Users.setPassword(loginCredentials)) {
						result.addProperty(Variables.result, "Something went wrong setting the new password. Try again");
						break reseting;						
					}
					
					result.addProperty(Variables.result, Variables.success);
				}
			}
			
		} catch (Exception e) {
			result.addProperty(Variables.result, Error.printStackTrace(e));
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- Setting new password -- Done");
	}

}
