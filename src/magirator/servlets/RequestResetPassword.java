package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

import magirator.logic.LoginCredentials;
import magirator.model.neo4j.Users;
import magirator.support.Encryption;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Mail;
import magirator.support.Constants;

/**
 * Servlet implementation class RequestResetPassword
 */
@WebServlet("/RequestResetPassword")
public class RequestResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- Requesting reset --");
		
		JsonObject result = new JsonObject();
		result.addProperty(Constants.result, "Username is incorrect");
		
		JsonObject credentials;
		
		try {
			credentials = Json.parseRequestData(request);
			
			LoginCredentials loginCredentials = new LoginCredentials(credentials);
			
			String code = Encryption.generateResetCode();
						
			if (Users.requestResetPassword(loginCredentials, code)){ //Det finns ett reset
				
				result.addProperty(Constants.result, "Something went wrong sending the reset mail");
				
				Mail.SendMail(loginCredentials.getUsername(), "Magirator password reset", "Use this code [ " + code + " ] in the reset password dialog.");
				result.addProperty(Constants.result, Constants.success);				
			}
			
		} catch (Exception e) {
			result.addProperty(Constants.result, Error.printStackTrace(e));
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- Requesting reset -- Done");
	}

}
