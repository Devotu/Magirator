package magirator.micros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Username or password is incorrect");
		
		JsonObject credentials = Json.parseRequestData(request);

		String username = Json.getString(credentials, "username", "");
		String password = Json.getString(credentials, "password", "");
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			con = Database.getConnection();

			String query = "MATCH (u:User) WHERE u.name = ? AND u.password = ? RETURN id(u)";

			ps = con.prepareStatement(query);
      		ps.setString(1, username);
      		ps.setString(2, password);

      		rs = ps.executeQuery();
		
			if (rs.next()) { //Användaren finns
				result.addProperty("result", "Login succeded but could not load Player");
				
				int userId = rs.getInt("id(u)"); rs.close();
				
				query = "MATCH (u:User)-[i:Is]->(p:Player) WHERE id(u) = ? RETURN id(p), PROPERTIES(p)";

				ps = con.prepareStatement(query);
				ps.setInt(1, userId);
				
				rs = ps.executeQuery();
			
				if (rs.next()) { //Success
					Player player = new Player( rs.getInt("id(p)"), (Map)rs.getObject("PROPERTIES(p)") );
					
					HttpSession session = request.getSession();
        			session.setAttribute("player", player);
        			
					result.addProperty("result", "Success");
				}
			}
			
	        response.setContentType("application/json");
	        response.getWriter().write(result.toString());
	        
			getServletContext().log("-- Login -- Done");
		
		} catch (Exception e){
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
