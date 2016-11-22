package magirator.model.neo4j;

import java.sql.*;
import java.util.*;
import javax.sql.*;

import javax.naming.*;

import magirator.beans.*;

public class AlterationHandler extends DatabaseHandler {
	
	DatabaseParams dp = null;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	
	public AlterationHandler(){
		this.dp = this.getDatabaseParameters();
	}	
	
	public List<ListItem> listAlterationsBelongingToDeck(int deckId)  throws Exception {
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();			

			String query = ""
					+ "MATCH dp=(d)<-[r:Evolved*]-() "
					+ "WHERE id(d)=? "
					+ "UNWIND nodes(dp) AS nd "
					+ "OPTIONAL MATCH (nd)<-[e:Evolved]-() "
					+ "RETURN DISTINCT id(nd), PROPERTIES(nd), PROPERTIES(e)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deckId);

      		ResultSet rs = ps.executeQuery();      		
      		
      		List<ListItem> alterationsList = new ArrayList<ListItem>();
      		
			while (rs.next()) {
				ListItem li = new ListItem();				
				HashMap sortables = new HashMap();
				HashMap filterables = new HashMap();
				Deck deck = new Deck(rs.getInt("id(nd)"), (Map)rs.getObject("PROPERTIES(nd)"));
				
				String displayName = "";
				
				if (rs.getObject("PROPERTIES(e)") == null){
					displayName = "Created " + deck.getTheme();
				} else {
					Map evolved = (Map)rs.getObject("PROPERTIES(e)");
					displayName = evolved.get("comment").toString();
				}
				
				li.setDisplayname(displayName);
				li.setId(deck.getDeckid());
				
				sortables.put("Date", deck.getDateCreated().getTime());
				
				li.setSortables(sortables);
				li.setFilterables(filterables);
				
				alterationsList.add(li);
			}
			
			return alterationsList;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}
	
	public Alteration getAlterationById(int alterationId) throws Exception {
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();			

			String query = "MATCH (d)<-[e:Evolved]-(pd) WHERE id(d)=? RETURN PROPERTIES(d), id(pd), PROPERTIES(pd), e.comment";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, alterationId);

      		ResultSet rs = ps.executeQuery();
      		
      		Alteration alteration = null;
			
			if (rs.next()) {
				Deck currentDeck = new Deck(alterationId, (Map)rs.getObject("PROPERTIES(d)"));
				Deck previousDeck = new Deck(rs.getInt("id(pd)"), (Map)rs.getObject("PROPERTIES(pd)"));
				String comment = rs.getString("e.comment");
				
				alteration = new Alteration(previousDeck, currentDeck, comment);
			}
			
			return alteration;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
	}
	
	
	public String[] getSortables(){
		
		String[] sortables = {"Date"};
		return sortables;
	}
	
	public HashMap getFilterables(){
		
		HashMap filterables = new HashMap();		
		return filterables;
	}

	public int AlterDeck(int deckId, String name, String format, String[] scolors, String theme, String comment) throws Exception {
		
		boolean[] colors = this.parseColors(scolors);
	
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();
			
			String query =
					"MATCH (u:User)-[r:Use]->(d:Deck) WHERE id(d) = ? "
					+ "SET d.active = false "
					+ "DELETE r "
					+ "CREATE (u)-[or:Used]->(d) "
					+ "CREATE (c:Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true}) "
					+ "CREATE (d)-[e:Evolved {comment:?}]->(c) "
					+ "CREATE (u)-[nr:Use]->(c) "
					+ "RETURN id(c)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, deckId);
			ps.setString(2, name);
			ps.setString(3, format);
			ps.setBoolean(4, colors[0]);//Black
			ps.setBoolean(5, colors[1]);//White
			ps.setBoolean(6, colors[2]);//Red
			ps.setBoolean(7, colors[3]);//Green
			ps.setBoolean(8, colors[4]);//Blue
			ps.setBoolean(9, colors[5]);//Colorless
			ps.setString(10, theme);
			ps.setString(11, comment);
			
			ResultSet rs = ps.executeQuery();
			
			int newDeckId = -1;
			
			if (rs.next()) {
				newDeckId = rs.getInt("id(c)");
			}
			
			return newDeckId;
			
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

	

}
