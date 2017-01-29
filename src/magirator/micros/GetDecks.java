package magirator.micros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import magirator.model.neo4j.DatabaseParams;
import magirator.objects.Deck;
import magirator.support.Database;
import magirator.support.Error;
import magirator.support.ParameterHelper;

/**
 * Servlet implementation class GetDecks
 */
@WebServlet(description = "Get all decks belonging to the current user", urlPatterns = { "/GetDecks" })
public class GetDecks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		getServletContext().log("-- GetDecks --");
		
		int playerId = ParameterHelper.returnParameter(req, "playerId", 0);
		
		DatabaseParams dp = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			String query = "MATCH (u:User)-[r:Use]->(d:Deck) WHERE id(u)=? RETURN id(d), PROPERTIES(d)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);

      		rs = ps.executeQuery();
    		
      		List<Deck> decks = new ArrayList<Deck>();
			
			while (rs.next()) {				
				decks.add(new Deck(rs.getInt("id(d)"), (Map)rs.getObject("PROPERTIES(d)")));
			}
			
            String returnJson = new Gson().toJson(decks);
            res.setContentType("application/json");
            res.getWriter().write(returnJson);
			
		} catch (Exception e){			
			res.getWriter().write( Error.printStackTrace(e) );
			
		} finally {
			
			try {
				
				if (rs != null) rs.close();
				if (st != null) st.close();
				if (con != null) con.close();
				
			} catch (SQLException e) {
				res.getWriter().write( Error.printStackTrace(e) );
			}
		}

		getServletContext().log("-- GetDecks -- Done");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
