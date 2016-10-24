package magirator.model.neo4j;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javax.sql.*;
import javax.naming.*;

import magirator.beans.*;

public class GameHandler extends DatabaseHandler {
	
	DatabaseParams dp = null;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	
	public GameHandler(){
		this.dp = this.getDatabaseParameters();
	}	
	
	public List<ListItem> listGamesBelongingToDeck(int deckId)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();			

			String query = "MATCH (d:Deck)-[p:Played]->(g:Game) WHERE id(d) = ? RETURN id(g), p.place, g.created";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deckId);

      		ResultSet rs = ps.executeQuery();
      		
      		List<ListItem> games = new ArrayList<ListItem>();
			
			while (rs.next()) {
				ListItem li = new ListItem();				
				HashMap sortables = new HashMap();
				HashMap filterables = new HashMap();
				
				Date date = new Date(rs.getLong("g.created"));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				li.setDisplayname(sdf.format(date));
				li.setId(rs.getInt("id(g)"));
				
				int place = rs.getInt("p.place");
				
				sortables.put("Date", date.getTime());
				sortables.put("Result", place);
				
				String result = "Loss";
				if (place == 1){
					result = "Win";
				}//TODO draw
				
				filterables.put("Result", result);
				
				li.setSortables(sortables);
				li.setFilterables(filterables);
				
				games.add(li);
			}

			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
			return games;
			
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public void addTwoPlayerGame(List<GameResult> results) throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();
			
			String query = "MATCH (d1:Deck), (d2:Deck)";
			query += "WHERE id(d1) = ? AND id(d2) = ?";
			query += "CREATE (d1)-[rw:Played {place: ?, comment: ?}]->(g:Game {created: TIMESTAMP()})<-[rl:Played {place:?}]-(d2)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, results.get(0).getDeckId());
			ps.setInt(2, results.get(1).getDeckId());
			ps.setInt(3, results.get(0).getPlace());
			ps.setString(4, results.get(0).getComment());
			ps.setInt(5, results.get(1).getPlace());
			
			ps.executeUpdate();			
			
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public Game getResultsInGame(int gameid) throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();			

			String query = 
					"MATCH (u:User)-->(d:Deck)-[p:Played]->(g:Game)" +
					"WHERE id(g) = ?" +
					"RETURN id(u), u.name, id(d), p.comment, id(g), g.created";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, gameid);

      		ResultSet rs = ps.executeQuery();
      		
      		List<GameResult> results = new ArrayList<GameResult>();
      		Game game = new Game();
      		
      		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      					
			while (rs.next()) {				
				
				game.setId(rs.getInt("id(g)"));
				
				Date date = new Date(rs.getLong("g.created"));
				game.setDatePlayed(sdf.format(date));
				
				GameResult gr = new GameResult();
				
				UserInfo ui = new UserInfo();
				ui.setId(rs.getInt("id(u)"));
				ui.setName(rs.getString("u.name"));
				gr.setUser(ui);
				
				gr.setComment(rs.getString("p.comment"));
				
				gr.setDeckId(rs.getInt("id(d)"));
				
				results.add(gr);
			}
			
			game.setResults(results);

			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
			return game;
			
		} catch (Exception ex){
			throw ex;
		}
	}
	
	
	
	
	
	
	
	
	//From copy of DeckHandler
	
	public void addDeckToUser(int userid, String name, String format, String[] scolors, String theme) throws Exception {
		
		boolean[] colors = this.parseColors(scolors);
	
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (u:User) WHERE id(u)=?";
			query += "CREATE (d:Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true})";
			query += "CREATE (u)-[r:Use]->(d)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, userid);
			ps.setString(2, name);
			ps.setString(3, format);
			ps.setBoolean(4, colors[0]);//Black
			ps.setBoolean(5, colors[1]);//White
			ps.setBoolean(6, colors[2]);//Red
			ps.setBoolean(7, colors[3]);//Green
			ps.setBoolean(8, colors[4]);//Blue
			ps.setBoolean(9, colors[5]);//Colorless
			ps.setString(10, theme);
			
			ps.executeUpdate();			
			
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
		} catch (Exception ex){
			throw ex;
		}
	}
	
	private boolean[] parseColors (String[] colors) {
	
		boolean[] parsedColors = new boolean[6];
		for (String color : colors) {
			switch (color) {
            			case "Black":  	parsedColors[0] = true;
                     				break;
            			case "White":  	parsedColors[1] = true;
                     				break;
            			case "Red":  	parsedColors[2] = true;
                     				break;
            			case "Green":  	parsedColors[3] = true;
                     				break;
            			case "Blue":  	parsedColors[4] = true;
                     				break;
            			case "Colorless":  	parsedColors[5] = true;
                     				break;
            			default: break;
        		}
		}
		
		return parsedColors;
	}
	
	public Deck getDeckById(int deckid)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (d:Deck) WHERE id(d)=? RETURN id(d), " +  
			"d.name, d.format, " +
			"d.black, d.white, d.red, d.green, d.blue, d.colorless, " +
			"d.theme, d.active, d.created";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deckid);

      		ResultSet rs = ps.executeQuery();
      		
      		Deck deck = null;
		
			while (rs.next()) {
				deck = new Deck(
					rs.getInt(1), 
					rs.getString("d.name"), 
					rs.getString("d.format"), 
					rs.getBoolean("d.black"), 
					rs.getBoolean("d.white"), 
					rs.getBoolean("d.red"), 
					rs.getBoolean("d.green"), 
					rs.getBoolean("d.blue"), 
					rs.getBoolean("d.colorless"),
					rs.getString("d.theme"),
					rs.getBoolean("d.active"),
					rs.getLong("d.created"));
			}

			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
			return deck;
			
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public String[] getSortables(){
		
		String[] sortables = {"Date", "Result"};
		return sortables;
	}
	
	public HashMap getFilterables(){
		
		HashMap filterables = new HashMap();
		
		String[] result = {"Win", "Draw", "Loss"};
		filterables.put("Result", result);
		String[] temps = {"Right", "Left"};
		filterables.put("Directions", temps);
		
		return filterables;
	}

	

}
