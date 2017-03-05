package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.naming.NamingException;
import magirator.dataobjects.Alteration;
import magirator.dataobjects.Deck;
import magirator.dataobjects.Player;
import magirator.support.Database;

public class Decks {

	public static boolean addDeck(Player player, Deck deck) throws SQLException, NamingException {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Database.getConnection();

			String query = "MATCH (p:Player) WHERE id(p)=?";
			query += "CREATE (d:Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true})";
			query += "CREATE (p)-[r:Use]->(d)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, player.getId());
			ps.setString(2, deck.getName());
			ps.setString(3, deck.getFormat());
			ps.setBoolean(4, deck.getBlack());
			ps.setBoolean(5, deck.getWhite());
			ps.setBoolean(6, deck.getRed());
			ps.setBoolean(7, deck.getGreen());
			ps.setBoolean(8, deck.getBlue());
			ps.setBoolean(9, deck.getColorless());
			ps.setString(10, deck.getTheme());

			ps.executeUpdate();

			return true;

		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

	public static ArrayList<Deck> getPlayerDecks(Player player) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Database.getConnection();

			String query = "MATCH (p:Player)-[r:Use]->(d:Deck) WHERE id(p)=? RETURN id(d), PROPERTIES(d)";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, player.getId());

			rs = ps.executeQuery();

			ArrayList<Deck> decks = new ArrayList<Deck>();

			while (rs.next()) {
				decks.add(new Deck(rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)")));
			}

			return decks;

		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

	public static Deck getDeck(int deckId) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Database.getConnection();

			String query = "MATCH (d:Deck) " + "WHERE id(d)=? " + "RETURN id(d), PROPERTIES(d)";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, deckId);

			rs = ps.executeQuery();

			Deck deck = null;

			if (rs.next()) {
				deck = new Deck(rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)"));
			}

			return deck;

		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

	public static boolean toggleDeck(int deckid) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Database.getConnection();

			String query = "MATCH (d:Deck) " + "WHERE id(d)=? " + "SET d.active = NOT d.active";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, deckid);

			int updates = ps.executeUpdate();

			if (updates >= 0) {
				return true;
			}

			return false;

		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

	public static boolean deleteDeck(int deckid) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Database.getConnection();

			String query = "MATCH (p:Player)-[use:Use]->(d:Deck) " + "WHERE id(d)=? " + "DELETE use "
					+ "CREATE (p)-[:Deleted]->(d)";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, deckid);

			int updates = ps.executeUpdate();

			if (updates > 0) {
				return true;
			}

			return false;

		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

	public static ArrayList<Alteration> getDeckAlterations(int deckId) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Database.getConnection();

			String query = "" + 
					"MATCH dp=(d)<-[r:Evolved*]-() " + 
					"WHERE id(d)=? " + 
					"UNWIND nodes(dp) AS nd " +  
			        "OPTIONAL MATCH (nd)<-[e:Evolved]-() " +  
					"RETURN DISTINCT id(nd), PROPERTIES(nd), PROPERTIES(e)" +
					"ORDER BY PROPERTIES(nd).created";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, deckId);

			rs = ps.executeQuery();

			Deck previousDeck = null;
			ArrayList<Alteration> alterations = new ArrayList<Alteration>();

			while (rs.next()) {

				Deck currentDeck = new Deck(rs.getInt("id(nd)"), (Map) rs.getObject("PROPERTIES(nd)"));
				String comment = "Created";

				if (previousDeck != null && rs.getObject("PROPERTIES(e)") != null) { // Inte första
					
					Map evolved = (Map) rs.getObject("PROPERTIES(e)");
					comment = evolved.get("comment").toString();
					
					alterations.add(new Alteration(previousDeck, currentDeck, comment));
				} else {
					alterations.add(new Alteration(currentDeck, currentDeck, comment));
				}

				previousDeck = currentDeck;
			}

			Collections.reverse(alterations);
			return alterations;

		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

	public static int AlterDeck(Deck newDeck, String comment) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			String query = "" +
					"MATCH (p:Player)-[r:Use]->(d:Deck) WHERE id(d) = ? " + 
					"SET d.active = false " + 
					"DELETE r " +
					"CREATE (p)-[:Used]->(d) " + 
					"CREATE (c:Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true}) " +
					"CREATE (d)-[e:Evolved {comment:?}]->(c) " + 
					"CREATE (p)-[nr:Use]->(c) " + 
					"RETURN id(c)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, newDeck.getDeckid());
			ps.setString(2, newDeck.getName());
			ps.setString(3, newDeck.getFormat());
			ps.setBoolean(4, newDeck.getBlack());
			ps.setBoolean(5, newDeck.getWhite());
			ps.setBoolean(6, newDeck.getRed());
			ps.setBoolean(7, newDeck.getGreen());
			ps.setBoolean(8, newDeck.getBlue());
			ps.setBoolean(9, newDeck.getColorless());
			ps.setString(10, newDeck.getTheme());
			ps.setString(11, comment);

			rs = ps.executeQuery();

			int newDeckId = -1;

			if (rs.next()) {
				newDeckId = rs.getInt("id(c)");
			}

			return newDeckId;

		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}
	
	public static Alteration getAlteration(int alterationId) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = "MATCH (d)<-[e:Evolved]-(pd) WHERE id(d)=? RETURN PROPERTIES(d), id(pd), PROPERTIES(pd), e.comment";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, alterationId);

      		rs = ps.executeQuery();
      		
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

}
