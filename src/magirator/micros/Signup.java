package magirator.micros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import magirator.model.neo4j.DatabaseParams;
import magirator.objects.Player;
import magirator.support.Database;
import magirator.support.Error;
import magirator.support.Json;

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
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			
			JsonObject signupRequest = Json.parseRequestData(request);
			String username = Json.getString(signupRequest, "username", "");

			String query = "MATCH (n:User) WHERE n.name = ? RETURN id(n)";

			ps = con.prepareStatement(query);
			ps.setString(1, username);

			rs = ps.executeQuery();
			
			if (!rs.next()){ //Namnet är redan i användning
				//TODO kontrollera playername också
				//TODO return hint
				
				String password = Json.getString(signupRequest, "password2", "");
				String playername = Json.getString(signupRequest, "playername", "");			
				
				query = "CREATE (u:User {name:?, password:?})-[c:Created {created: TIMESTAMP()}]->(p:Player { name: ? }) RETURN id(p), PROPERTIES(p)";
				
				ps = con.prepareStatement(query);

				ps.setString(1, username);
				ps.setString(2, password);
				ps.setString(3, playername);
			
				rs = ps.executeQuery();
				
				JsonObject result = new JsonObject();
				result.addProperty("result", "failure");
				
				if (rs.next()){ //Success
					
					result.addProperty("result", "success");					
				}
				
		        response.setContentType("application/json");
		        response.getWriter().write(result.toString());
			}
		} catch (NamingException e) {
			response.getWriter().write( Error.printStackTrace(e) );
		} catch (SQLException e) {
			response.getWriter().write( Error.printStackTrace(e) );
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				response.getWriter().write( Error.printStackTrace(e) );
			}
		}
	}

}
