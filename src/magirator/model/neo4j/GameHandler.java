package magirator.model.neo4j;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javax.sql.*;

import magirator.dataobjects.*;

import javax.naming.*;

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
				
				String result = getPlaceToReadable(place);
				li.setColorCode(getPlaceColorCode(place));
				
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
					"RETURN id(u), PROPERTIES(u), id(d), PROPERTIES(d), id(p), PROPERTIES(p), PROPERTIES(g)" +
					"ORDER BY p.place";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, gameid);      		

      		ResultSet rs = ps.executeQuery();
      		
      		GameResult gameResult = new GameResult(null, new ArrayList<Result>());
      		
      		while (rs.next()) {
      			if	(gameResult.getGame() == null){
      				gameResult.setGame(new Game((Map)rs.getObject("PROPERTIES(g)")));
      			}
      			Player u = new Player(rs.getInt("id(u)"), (Map)rs.getObject("PROPERTIES(u)"));
      			Deck d = new Deck(rs.getInt("id(d)"), (Map)rs.getObject("PROPERTIES(d)"));
      			Play p = new Play(rs.getInt("id(p)"), (Map)rs.getObject("PROPERTIES(p)"));
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
	
	
	public List<ListItem> listPlayerGamesNotConfirmed(int playerId)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();			

			String query = "MATCH (u:User) "
					+ "WHERE id(u) = ? "
					+ "MATCH (u)-[:Use]->(d:Deck) "
					+ "MATCH (d)-[p:Played]->(g:Game) "
					+ "WHERE p.confirmed = 0 "
					+ "RETURN id(g), p.place, g.created ";


      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);

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
				
				String result = getPlaceToReadable(place);
				li.setColorCode(getPlaceColorCode(place));
				
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

	private String getPlaceToReadable(int place){
		String result = "Loss";
		if (place == 1){
			return "Win";
		} else if (place == 0){
			return "Draw";
		} else {
			return "Loss";
		}
	}
	
	private int getPlaceColorCode(int place){
		String result = "Loss";
		if (place == 1){
			return 5;
		} else if (place == 0){
			return 3;
		} else {
			return 1;
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
		
		return filterables;
	}
	
	public void confirmGame(int playId, boolean confirm, String comment) throws Exception{
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();			
			
			String query = "MATCH (d:Deck)-[p:Played]->(g:Game) "
					+ "WHERE id(p)=? "
					+ "SET p.confirmed=?, p.comment=?";
			
			PreparedStatement ps = con.prepareStatement(query);
      		
			ps.setInt(1, playId);
			ps.setInt(2, confirm ? 1 : -1);
			ps.setString(3, comment);
			
			ps.executeUpdate();
			
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
		} catch (Exception ex){
			throw ex;
		}
		
		
	}

}
