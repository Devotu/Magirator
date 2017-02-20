package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import magirator.dataobjects.Deck;
import magirator.dataobjects.Game;
import magirator.dataobjects.ListItem;
import magirator.dataobjects.Participant;
import magirator.dataobjects.Play;
import magirator.dataobjects.Player;
import magirator.dataobjects.Result;
import magirator.support.Database;

public class Games {
	
	public static boolean addGame(ArrayList<Result> results) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "CREATE (g:Game {created: TIMESTAMP()}) RETURN id(g)";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			if	(rs.next()){
				int gameId = rs.getInt("id(g)");
				
				query = "MATCH (g:Game), (d:Deck)";
				query += "WHERE id(g) = ? AND id(d) = ?";
				query += "CREATE (d)-[r:Played {place: ?, comment: ?, confirmed:?, added:? }]->(g)";					
				ps = con.prepareStatement(query);
				
				for (Result r : results){
					ps.setInt(1, gameId);
					ps.setInt(2, r.getDeck().getDeckid());
					ps.setInt(3, r.getPlay().getPlace());
					ps.setString(4, r.getPlay().getComment());
					ps.setInt(5, r.getPlay().getConfirmed() ? 1 : 0);
					ps.setLong(6, r.getPlay().getAdded().getTime() );
					
					ps.executeUpdate();						
				}
				
				return true;			
			}
			
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
		} catch (Exception ex){
			throw ex;
		}
		
		return false;
	}

	public static ArrayList<Play> getDeckPlayed(Deck deck) throws NamingException, SQLException {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			String query = "MATCH (d:Deck)-[p:Played]->(:Game) WHERE id(d)=? RETURN id(p), PROPERTIES(p)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deck.getDeckid());

      		rs = ps.executeQuery();
      		
      		ArrayList<Play> plays = new ArrayList<Play>();
			
			while (rs.next()) {
				plays.add( new Play( rs.getInt("id(p)"), (Map)rs.getObject("PROPERTIES(p)") ) );
			}

			return plays;
			
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}

	}

	public static ArrayList<Participant> getDeckParticipations(int deckId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (pl:Player)-[Use]->(d:Deck)-[p:Played]->(g:Game) " //TODO Used & Evolved
					+ "WHERE id(d) = ? "
					+ "RETURN id(pl), PROPERTIES(pl), id(d), PROPERTIES(d), id(p), PROPERTIES(p), id(g), PROPERTIES(g) "
					+ "ORDER BY g.created";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, deckId);

      		rs = ps.executeQuery();
      		
      		ArrayList<Participant> participations = new ArrayList<Participant>();
			
			while (rs.next()) {
				Player player = new Player( rs.getInt("id(pl)"), (Map) rs.getObject("PROPERTIES(pl)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Play play = new Play( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				participations.add(new Participant(player, deck, play, game));
			}

			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return participations;
			
		} catch (Exception ex){
			throw ex;
		}
	}

	public static ArrayList<Participant> getParticipants(int gameId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (pl:Player)-[Use]->(d:Deck)-[p:Played]->(g:Game) " //TODO Used & Evolved
					+ "WHERE id(g) = ? "
					+ "RETURN id(pl), PROPERTIES(pl), id(d), PROPERTIES(d), id(p), PROPERTIES(p), id(g), PROPERTIES(g) "
					+ "ORDER BY p.place";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, gameId);

      		rs = ps.executeQuery();
      		
      		ArrayList<Participant> participants = new ArrayList<Participant>();
			
			while (rs.next()) {
				Player player = new Player( rs.getInt("id(pl)"), (Map) rs.getObject("PROPERTIES(pl)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Play play = new Play( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				participants.add(new Participant(player, deck, play, game));
			}

			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return participants;
			
		} catch (Exception ex){
			throw ex;
		}
	}

	public static ArrayList<Participant> getUnconfirmedGames(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (pl:Player) "
					+ "WHERE id(pl) = ? "
					+ "MATCH (pl)-[:Use]->(d:Deck)-[p:Played]->(g:Game) "
					+ "WHERE p.confirmed = 0 "
					+ "RETURN id(pl), PROPERTIES(pl), id(d), PROPERTIES(d), id(p), PROPERTIES(p), id(g), PROPERTIES(g)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);

      		rs = ps.executeQuery();
      		
      		rs = ps.executeQuery();
      		
      		ArrayList<Participant> participants = new ArrayList<Participant>();
			
			while (rs.next()) {
				Player player = new Player( rs.getInt("id(pl)"), (Map) rs.getObject("PROPERTIES(pl)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Play play = new Play( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				participants.add(new Participant(player, deck, play, game));
			}

			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return participants;
			
		} catch (Exception ex){
			throw ex;
		}
	}

	public static boolean confirmGame(int playId, boolean confirm, String comment) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (d:Deck)-[p:Played]->(g:Game) "
					+ "WHERE id(p)=? "
					+ "SET p.confirmed=?, p.comment=?";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, playId);
			ps.setInt(2, confirm ? 1 : -1);
			ps.setString(3, comment);
			
			ps.executeUpdate();
			
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return true;
			
		} catch (Exception ex){
			throw ex;
		}
	}

}




























