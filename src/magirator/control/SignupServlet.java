package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import magirator.dataobjects.*;
import magirator.model.neo4j.*;

public class SignupServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- Signup --");
		getServletContext().log("-  Signup -> Collecting data");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		SignupHandler signupHandler = new SignupHandler();
		getServletContext().log("-  Signup -> Signing up");
		
		response.setContentType("text/plain");	
		
		try {
			String returnName = signupHandler.signup(username, password);
			if (returnName != null) {
				response.getWriter().write(returnName);
				getServletContext().log("-  Signup -> Signup succeded for user " + returnName);
			} else {        			
        		getServletContext().log("-  Signup -> Signup failed for username " + username);
			}
				
		} catch (Exception ex) {
			getServletContext().log("-  Signup -- Error -- " + ex.getMessage());
			throw new ServletException("Something databazy went wrong");
		}

		getServletContext().log("--  Signup -- Done");
	}
}


		
		
	

