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
			
			/*
			while (rs.next()) {
				ListItem li = new ListItem();				
				HashMap sortables = new HashMap();
				HashMap filterables = new HashMap();
				Deck deck = new Deck((Map)rs.getObject("PROPERTIES(pd)"), rs.getInt("id(pd)"));
				
				String displayName = "";
				
				if (previousDeck == null){
					displayName = "Created ";
				} else {
					displayName = "Altered ";
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				li.setDisplayname(displayName += sdf.format(deck.getDateCreated()));
				li.setId(deck.getDeckid());
				
				sortables.put("Date", deck.getDateCreated().getTime());
				
				li.setSortables(sortables);
				li.setFilterables(filterables);
				
				alterationsList.add(li);
				previousDeck = deck;
			}

			query = "MATCH (d:Deck) WHERE id(d)=? RETURN PROPERTIES(d)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, deckId);

      		rs = ps.executeQuery();
		
			if (rs.next()) {
				ListItem li = new ListItem();				
				HashMap sortables = new HashMap();
				HashMap filterables = new HashMap();
				Deck deck = new Deck((Map)rs.getObject("PROPERTIES(d)"), deckId);
				
				String displayName = "Altered to present ";
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				li.setDisplayname(displayName += sdf.format(deck.getDateCreated()));
				li.setId(deck.getDeckid());
				
				sortables.put("Date", deck.getDateCreated().getTime());
				
				li.setSortables(sortables);
				li.setFilterables(filterables);
				
				alterationsList.add(li);				
			}
			*/
			
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



	

}
