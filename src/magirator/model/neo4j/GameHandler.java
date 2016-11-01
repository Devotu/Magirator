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
					li.setColorCode(5);
				} else if (place == 0){
					result = "Draw";
					li.setColorCode(3);
				} else {
					result = "Loss";
					li.setColorCode(1);
				}
				
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
	
	public void addGame(List<Result> results) throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();			
			
			String query = "CREATE (g:Game {created: TIMESTAMP()}) RETURN id(g)";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			if	(rs.next()){
				int gameId = rs.getInt("id(g)");
				
				for (Result r : results){
					query = "MATCH (g:Game), (d:Deck)";
					query += "WHERE id(g) = ? AND id(d) = ?";
					query += "CREATE (d)-[r:Played {place: ?, comment: ?, confirmed:? }]->(g)";					
					ps = con.prepareStatement(query);
					
					ps.setInt(1, gameId);
					ps.setInt(2, r.getDeck().getDeckid());
					ps.setInt(3, r.getPlay().getPlace());
					ps.setString(4, r.getPlay().getComment());
					ps.setInt(5, r.getPlay().getConfirmed());
					
					ps.executeUpdate();						
				}
						
			}
			
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
		} catch (Exception ex){
			throw ex;
		}
	}
	
	public GameResult getResultsInGame(int gameid) throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();			

			String query =
					"MATCH (u:User)-->(d:Deck)-[p:Played]->(g:Game)" +
					"WHERE id(g) = ?" +
					"RETURN PROPERTIES(u), PROPERTIES(d), PROPERTIES(p), PROPERTIES(g)" +
					"ORDER BY p.place";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, gameid);      		

      		ResultSet rs = ps.executeQuery();
      		
      		GameResult gameResult = new GameResult(null, new ArrayList<Result>());
      		
      		while (rs.next()) {
      			if	(gameResult.getGame() == null){
      				gameResult.setGame(new Game((Map)rs.getObject("PROPERTIES(g)")));
      			}
      			Player u = new Player((Map)rs.getObject("PROPERTIES(u)"));
      			Deck d = new Deck((Map)rs.getObject("PROPERTIES(d)"));
      			Play p = new Play((Map)rs.getObject("PROPERTIES(p)"));
      			Result r = new Result(d, p, u);
      			gameResult.addResult(r);
      		}

			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
			return gameResult;
			
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
