package magirator.model.neo4j;

import java.sql.*;
import java.util.*;

import javax.sql.*;
import javax.naming.*;

import magirator.beans.*;

public class DeckHandler extends DatabaseHandler {
	
	DatabaseParams dp = null;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	
	public DeckHandler(){
		this.dp = this.getDatabaseParameters();
	}

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
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
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
	
	public List<ListItem> listDecksBelongingToUser(int userid)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (u:User)-[r:Use]->(d:Deck) WHERE id(u)=? RETURN d.name, id(d), d.format";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, userid);

      		ResultSet rs = ps.executeQuery();
      		
      		List<ListItem> decks = new ArrayList<ListItem>();
			
			while (rs.next()) {
				ListItem li = new ListItem();				
				HashMap sortables = new HashMap();
				HashMap filterables = new HashMap();
				
				li.setDisplayname(rs.getString(1));
				sortables.put("Name", li.getDisplayname());
				li.setId(rs.getInt(2));
				sortables.put("Winrate", 50);
				filterables.put("Format", rs.getString(3));
				
				li.setSortables(sortables);
				li.setFilterables(filterables);
				
				decks.add(li);
				//decks.add(new ListDeck(rs.getString(1), rs.getInt(2), 50, rs.getString(3))); //TODO Winrate
			}

			return decks;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}
	
	public Deck getDeckById(int deckid)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (d:Deck) "
					+ "WHERE id(d)=? "
					+ "RETURN id(d), PROPERTIES(d)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deckid);

      		ResultSet rs = ps.executeQuery();
      		
      		Deck deck = null;
		
			while (rs.next()) {
				deck = new Deck(rs.getInt("id(d)"), (Map)rs.getObject("PROPERTIES(d)"));
			}

			return deck;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}
	
	public String[] getSortables(){
		
		String[] sortables = {"Name", "Winrate", "Games"};
		return sortables;
	}
	
	public HashMap getFilterables(){
		
		HashMap filterables = new HashMap();
		
		String[] format = {"Standard", "Block", "Pauper"};
		filterables.put("Format", format);
		
		String[] active = {"Active", "Not Active"};
		filterables.put("Active", active);
		
		return filterables;
	}
	
	public boolean deleteDeck(int deckid)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (u:User)-[or:Use]->(d:Deck) "
					+ "WHERE id(d)=? "
					+ "DELETE or "
					+ "CREATE (u)-[nr:Used]->(d) ";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deckid);

      		int updates = ps.executeUpdate();

			if(updates > 0){
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}
	
	public boolean toggleDeck(int deckid)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (d:Deck) "
					+ "WHERE id(d)=? "
					+ "SET d.active=("
					+ "	CASE d.active"
					+ "		WHEN true THEN false"
					+ "		ELSE true"
					+ "	END"
					+ ")";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deckid);

      		int updates = ps.executeUpdate();

			if(updates > 0){
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}

}
