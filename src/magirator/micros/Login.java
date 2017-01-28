package magirator.micros;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import magirator.model.neo4j.LoginHandler;
import magirator.objects.Deck;
import magirator.objects.Player;
import magirator.support.Database;
import magirator.support.Error;
import magirator.support.Json;

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
		
		JsonObject credentials = Json.parseRequestData(request);
		
		String username = (String) (credentials.has("username") ? credentials.get("username").getAsString() : "");
		String password = (String) (credentials.has("password") ? credentials.get("password").getAsString() : "");
		
		/*
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;		
		
		try {	
			con = Database.getConnection();

			String query = "MATCH (n:User) WHERE n.name = ? AND n.password = ? RETURN id(n)";

			PreparedStatement ps = con.prepareStatement(query);
      		ps.setString(1, username);
      		ps.setString(2, password);

      		rs = ps.executeQuery();
		
			if (rs.next()) { //Success
				
				
				
				HttpSession session = request.getSession();
				passwordFromDatabase = rs.getString("n.password");
			}
		
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
		
		
		
		
		try {
			userid = loginHandler.login(username, password);
			if (userid >= 0) {
				getServletContext().log("-  Login -> login succeded for user " + userid + "(" + username + ")");
				HttpSession session = request.getSession();
				Player player = new Player(username, userid);
        			session.setAttribute("player", player);
			} else {        			
        			getServletContext().log("-  Login -> login failed, user is: " + userid + "(" + username + ")");
				viewName = "/Welcome.jsp";
			}
			
			RequestDispatcher d = getServletContext().getRequestDispatcher(viewName);

			if (d != null) {
				getServletContext().log("-  Login -> Dispatching to View" + viewName);
				d.forward(request, response);
			} else {
				getServletContext().log("-  Login -> No view named " + viewName);
			}
			
		} catch (Exception ex) {
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			String prettyException = "<br>" + sw.toString();
			prettyException = prettyException.replace("\n", "<br>");
			
			request.setAttribute("exception", prettyException);

			RequestDispatcher d = getServletContext().getRequestDispatcher(errorViewName);

			if (d != null) {
				getServletContext().log("-  Login -> Dispatching to errorView " + errorViewName);
				d.forward(request, response);
			} else {
				getServletContext().log("-  Login -> No errorView named " + errorViewName);
			}
		}

		getServletContext().log("-- Login -- Done");
		
		try {	
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (n:User) WHERE n.name = ? RETURN n.password";

			PreparedStatement ps = con.prepareStatement(query);
      		ps.setString(1, username);

      		rs = ps.executeQuery();
		
			String passwordFromDatabase = "none";
		
			if (rs.next()) {
				passwordFromDatabase = rs.getString("n.password");
			}
			
			if (password.equals(passwordFromDatabase)) {
				
				query = "MATCH (n:User) WHERE n.name = ? RETURN id(n)";
				ps = con.prepareStatement(query);
				ps.setString(1, username);

				rs = ps.executeQuery();
				
				if (rs.next()) {
					return rs.getInt(1);
				} else { 
					return -1;
				}

			} else {
				return -1;
			}
		
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
		
		*/
	}

}
