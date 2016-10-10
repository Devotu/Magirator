package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.*;

import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;

public class LoginServlet_debug extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- Login --");
		getServletContext().log("-  Login -> Collecting data");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String viewName = request.getParameter("goView");
		String errorViewName = request.getParameter("errorView");
		int userid = -1;
		
		
		// Test
		try {
			String ts = testDatabase("Dolph", "Tops");
		} catch (Exception ex) {
			getServletContext().log("test error: " + ex.getMessage());
		}
		
		
		//
		
		LoginHandler loginHandler = new LoginHandler();
		getServletContext().log("-  Login -> Logging in " + username + " with pwd " + password);		
		
		try {
			userid = loginHandler.login(username, password);
			if (userid >= 0) {
				getServletContext().log("-  Login -> login succeded for user " + userid + "(" + username + ")");
				HttpSession session = request.getSession();
				UserInfo userInfo = new UserInfo(username, userid);
        			session.setAttribute("userInfo", userInfo);
			} else {        			
        			getServletContext().log("-  Login -> login failed, user is: " + userid + "(" + username + ")");
				viewName = "/Welcome.jsp";
			}
				
		} catch (Exception ex) {
			getServletContext().log("-  Login -- Error -- " + ex.getMessage());
			throw new ServletException("Something databazy went wrong");

		}
		
		try {
			
			RequestDispatcher d = getServletContext().getRequestDispatcher(viewName);

			if (d != null) {
				getServletContext().log("-  Login -> Dispatching to View" + viewName);
				d.forward(request, response);
			} else {
				getServletContext().log("-  Login -> No view named " + viewName);
			}
		} catch (Exception ex) {
			request.setAttribute("exception", ex.toString());

			RequestDispatcher d = getServletContext().getRequestDispatcher(errorViewName);

			if (d != null) {
				getServletContext().log("-  Login -> Dispatching to errorView " + errorViewName);
				d.forward(request, response);
			} else {
				getServletContext().log("-  Login -> No errorView named " + errorViewName);
			}
		}

		getServletContext().log("-- Login -- Done");
	}
	
	private String testDatabase(String username, String password) throws SQLException, NamingException {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {	
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();
			
			st = con.createStatement();
			rs = st.executeQuery("MATCH (n:User) WHERE n.name = '" + username + "' RETURN id(n)");
		
			String passwordFromDatabase = "none";
		
			if (rs.next()) {
				getServletContext().log("DEBUG --> Got result " + rs.getInt(1));
				
				//passwordFromDatabase = rs.getString("n.password");
			}
			
			if (password.equals(passwordFromDatabase)) {
				getServletContext().log("DEBUG --> Matchy");;							
			} else {
				getServletContext().log("DEBUG --> No match");
			}
		
		} catch (Exception ex){
			getServletContext().log("DEBUG --> Exception: " + ex.getMessage());
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
		
		return "survived";
	}
}


		
		
	

