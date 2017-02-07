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

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import magirator.dataobjects.Deck;
import magirator.dataobjects.Game;
import magirator.dataobjects.Play;
import magirator.dataobjects.Player;
import magirator.model.neo4j.DatabaseParams;
import magirator.model.neo4j.Decks;
import magirator.model.neo4j.Games;
import magirator.support.Database;
import magirator.support.Error;
import magirator.support.ParameterHelper;
import magirator.support.Variables;
import magirator.viewobjects.ListDeck;

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

		getServletContext().log("-- GetDecks --");
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Could not get decks, are you logged in?");
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		//Player is logged in
		if (player != null){
			
			try {
				ArrayList<Deck> decks = Decks.getPlayerDecks(player);
				
				//There are decks
				if (decks != null && decks.size() > 0){
					
					ArrayList<ListDeck> listDecks = new ArrayList<ListDeck>();
					
					for	(Deck d : decks){
						
						ArrayList<Play> plays = Games.getDeckPlayed(d);
						
						float wins = 0;
						float games = 0;
						
						for (Play p : plays){
							
							if (p.getConfirmed()){
								
								games++;
								
								if (p.getPlace() == 1){ //Win
									wins++;
								}
							}
						}
						
						int winrate = 0;
						
						if (games > 0) {
							winrate = Math.round((wins / games) * 100);
						}
						
						listDecks.add(new ListDeck(d, winrate, (int)games));					
					}
					
					result.addProperty("decks", new Gson().toJson(listDecks));
				}
				
				result.addProperty(Variables.result, Variables.success);
				
			} catch (Exception e) {
				result.addProperty(Variables.result, Error.printStackTrace(e));
			}
			
		} else {
			
			result.addProperty(Variables.result, "Failed to get games, please login");
		}
		
		response.setContentType("application/json");
		response.getWriter().write(result.toString());

		getServletContext().log("-- GetDecks -- Done");
	}

}
