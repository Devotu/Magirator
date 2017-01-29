package magirator.micros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
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
		
		getServletContext().log("-- Signup --");
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			
			JsonObject result = new JsonObject();
			result.addProperty("result", "Username already in use");
			
			JsonObject signupRequest = Json.parseRequestData(request);
			String username = Json.getString(signupRequest, "username", "");

			String query = "MATCH (n:User) WHERE n.name = ? RETURN id(n)";

			ps = con.prepareStatement(query);
			ps.setString(1, username);

			rs = ps.executeQuery(); ps.close();

			
			if (!rs.next()){ rs.close(); //Namnet är inte i användning
				result.addProperty("result", "Player name already in use");				

				String playername = Json.getString(signupRequest, "playername", "");			
				
				query = "MATCH (p:Player) WHERE p.name = ? RETURN id(p)";

				ps = con.prepareStatement(query);
				ps.setString(1, playername);

				rs = ps.executeQuery(); ps.close();
				
				if (!rs.next()){ rs.close(); //Playername är inte i användning
				result.addProperty("result", "Error adding user and player");
					
					String password = Json.getString(signupRequest, "password2", "");		
					
					query = "CREATE (u:User {name:?, password:?, created: TIMESTAMP()})-[c:Created {created: TIMESTAMP()}]->(p:Player { name: ? }) "
							+ "CREATE (u)-[i:Is]->(p)"
							+ "RETURN id(p)";
					
					ps = con.prepareStatement(query);

					ps.setString(1, username);
					ps.setString(2, password);
					ps.setString(3, playername);
				
					rs = ps.executeQuery(); ps.close();
					
					if (rs.next()){ rs.close(); //Success
						
						result.addProperty("result", "Success");					
					}					

				}
						        
			}
			
	        response.setContentType("application/json");
	        response.getWriter().write(result.toString());
	        
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
		
		getServletContext().log("-- Signup Done --");
	}

}
