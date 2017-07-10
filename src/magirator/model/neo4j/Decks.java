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

import magirator.data.collections.Alteration;
import magirator.data.entities.Deck;
import magirator.data.entities.Minion;
import magirator.data.entities.Player;
import magirator.data.interfaces.IPlayer;
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
			ps.setLong(4, deck.getBlackCards());
			ps.setLong(5, deck.getWhiteCards());
			ps.setLong(6, deck.getRedCards());
			ps.setLong(7, deck.getGreenCards());
			ps.setLong(8, deck.getBlueCards());
			ps.setLong(9, deck.getColorlessCards());
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

			//String query = "MATCH (p:Player)-[r:Use]->(d:Deck) WHERE id(p)=? RETURN id(d), PROPERTIES(d)";

			String query = ""
					+ "MATCH (p:Player)-[:Use]->(d:Deck) "
					+ "WHERE id(p) = ? WITH collect(d) as d1 "
					+ "OPTIONAL MATCH (d:Deck)<-[:Use]-(m:Minion) "
					+ "WHERE id(m) = ? WITH collect(d) + d1 as d2 "
					+ "UNWIND d2 as deck "
					+ "RETURN PROPERTIES(deck)";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, player.getId());
			ps.setInt(2, player.getId());

			rs = ps.executeQuery();

			ArrayList<Deck> decks = new ArrayList<Deck>();

			while (rs.next()) {
				decks.add(new Deck((Map <String, ?>) rs.getObject("PROPERTIES(deck)")));
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
				deck = new Deck((Map) rs.getObject("PROPERTIES(d)"));
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

			ps.setInt(1, newDeck.getId());
			ps.setString(2, newDeck.getName());
			ps.setString(3, newDeck.getFormat());
			ps.setLong(4, newDeck.getBlackCards());
			ps.setLong(5, newDeck.getWhiteCards());
			ps.setLong(6, newDeck.getRedCards());
			ps.setLong(7, newDeck.getGreenCards());
			ps.setLong(8, newDeck.getBlueCards());
			ps.setLong(9, newDeck.getColorlessCards());
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
	
	public static Deck addMinionDeck(IPlayer minion, Deck deck) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Database.getConnection();

			String query = "MATCH (m:Minion) WHERE id(m)=?";
			query += "CREATE (d:Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true})";
			query += "CREATE (m)-[r:Use]->(d) ";
			query += "RETURN id(d), PROPERTIES(d)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, minion.getId());
			ps.setString(2, deck.getName());
			ps.setString(3, deck.getFormat());
			ps.setLong(4, deck.getBlackCards());
			ps.setLong(5, deck.getWhiteCards());
			ps.setLong(6, deck.getRedCards());
			ps.setLong(7, deck.getGreenCards());
			ps.setLong(8, deck.getBlueCards());
			ps.setLong(9, deck.getColorlessCards());
			ps.setString(10, deck.getTheme());

      		rs = ps.executeQuery();
      		
      		Alteration alteration = null;
			
			if (rs.next()) {
				return new Deck(rs.getInt("id(d)"), (Map)rs.getObject("PROPERTIES(d)"));
			}
			
			return null;

		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

	public static Deck getPlayerDeckInLiveGame(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (p:Player) "
					+ "WHERE id(p) = ? "
					+ "MATCH (p)-[:Use|:Used]->(d:Deck)-[:Got]->(:Result)-[:In]->(:Game)-[:Runs]->(:Live) "
					+ "RETURN id(d), PROPERTIES(d)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		
      		rs = ps.executeQuery();
      		
			if (rs.next()) {
				return new Deck(rs.getInt("id(d)"), (Map)rs.getObject("PROPERTIES(d)"));
			}
			
			return null;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

}