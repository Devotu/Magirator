package magirator.model.neo4j;

import java.sql.*;
import java.util.*;

import javax.sql.*;

import magirator.objects.*;

import javax.naming.*;

public class PlayerHandler extends DatabaseHandler {
	
	DatabaseParams dp = null;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	
	public PlayerHandler(){
		this.dp = this.getDatabaseParameters();
	}	
	
	public List<Opponent> getPlayerOpponents(int userid)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			//TODO Get more specific
			String query = "MATCH (u:User)" +
				"WHERE NOT id(u) = ?" +
				"MATCH (u)-->(d:Deck)" +
				"RETURN u.name, id(u), d.name, id(d)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, userid);

      		ResultSet rs = ps.executeQuery();
      		
      		List<Opponent> opponents = new ArrayList<Opponent>();
			
			while (rs.next()) {
				Opponent o = new Opponent();
				o.setId(rs.getInt("id(u)"));
				
				int idx = opponents.indexOf(o);
				if	(idx != -1){ //If in list add deck 
					opponents.get(idx).addDeck(rs.getString("d.name"), rs.getInt("id(d)"));
					opponents.get(idx).addDeckString(rs.getString("d.name"), rs.getInt("id(d)"));
				} else { //If not in list, add
					o.setName(rs.getString("u.name"));
					o.addDeck(rs.getString("d.name"), rs.getInt("id(d)"));
					o.addDeckString(rs.getString("d.name"), rs.getInt("id(d)"));
					opponents.add(o);
				}
			}

			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
			
			return opponents;
			
		} catch (Exception ex){
			throw ex;
		}
	}

	public Player getPlayerByDeck(int deckId) throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (u:User)-->(d:Deck) WHERE id(d) = ? RETURN id(u), PROPERTIES(u)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deckId);

      		ResultSet rs = ps.executeQuery();
      		
      		Player player = null;
		
			if (rs.next()) {
				player = new Player(rs.getInt("id(u)"), (Map)rs.getObject("PROPERTIES(u)"));
			}
			
			return player;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}

}
