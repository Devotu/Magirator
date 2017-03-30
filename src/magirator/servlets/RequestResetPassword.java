package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;

import magirator.model.neo4j.Users;
import magirator.support.Error;
import magirator.support.Json;
import magirator.support.Mail;
import magirator.support.Variables;
import magirator.viewobjects.LoginCredentials;

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
		result.addProperty(Variables.result, "Username is incorrect");
		
		JsonObject credentials;
		
		try {
			credentials = Json.parseRequestData(request);
			
			//TODO 
			LoginCredentials loginCredentials = new LoginCredentials(credentials);
			
			//TODO Skapa kod
			String code = "coden";
						
			if (Users.requestResetPassword(loginCredentials, code)){ //Det finns ett reset
				
				Mail.SendMail(loginCredentials.getUsername(), "Magirator password reset", "Use this code [ " + code + " ] in the reset password dialog.");
				result.addProperty(Variables.result, Variables.success);				
			}
			
		} catch (Exception e) {
			result.addProperty(Variables.result, Error.printStackTrace(e));
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- Requesting reset -- Done");
	}

}
