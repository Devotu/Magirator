package magirator.model.neo4j;

import java.sql.*;
import java.text.SimpleDateFormat;
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

			String query = "MATCH p=(d)<-[:Evolved*]-(pd) WHERE id(d)=? WITH DISTINCT pd RETURN PROPERTIES(pd)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deckId);

      		ResultSet rs = ps.executeQuery();      		
      		
      		List<ListItem> alterationsList = new ArrayList<ListItem>();
      		Deck previousDeck = null;
			
			while (rs.next()) {
				ListItem li = new ListItem();				
				HashMap sortables = new HashMap();
				HashMap filterables = new HashMap();
				Deck deck = new Deck((Map)rs.getObject("PROPERTIES(pd)"));
				
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
				Deck deck = new Deck((Map)rs.getObject("PROPERTIES(d)"));
				
				String displayName = "Altered to present ";
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				li.setDisplayname(displayName += sdf.format(deck.getDateCreated()));
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

			String query = "MATCH (d)<-[:Evolved]-(pd) WHERE id(d)=? RETURN PROPERTIES(d), PROPERTIES(pd)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, alterationId);

      		ResultSet rs = ps.executeQuery();
      		
      		Alteration alteration = null;
			
			if (rs.next()) {
				
				alteration = new Alteration((Map)rs.getObject("PROPERTIES(pd)"), (Map)rs.getObject("PROPERTIES(d)"));
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
