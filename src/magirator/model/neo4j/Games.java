package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.NamingException;

import magirator.data.collections.Participant;
import magirator.data.interfaces.IPlayer;
import magirator.data.objects.Deck;
import magirator.data.objects.Game;
import magirator.data.objects.Player;
import magirator.data.objects.Result;
import magirator.support.Database;

public class Games {
	
	public static int addGame(ArrayList<Participant> participants, boolean draw, int initiatorId) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "CREATE (g:Game {created: TIMESTAMP(), draw: ?}) RETURN id(g)";
			
			ps = con.prepareStatement(query);
			
			ps.setBoolean(1, draw);
			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				int gameId = rs.getInt("id(g)");
				
				query = "MATCH (g:Game), (d:Deck) ";
				query += "WHERE id(g) = ? AND id(d) = ?";
				query += "CREATE (d)-[:Got]->(r:Result {place: ?, comment: ?, confirmed:?, added:? })-[:In]->(g)";
				
				ps = con.prepareStatement(query);
				
				for (Participant p : participants){
					ps.setInt(1, gameId);
					ps.setInt(2, p.getDeck().getDeckid());
					ps.setInt(3, p.getResult().getPlace());
					ps.setString(4, p.getResult().getComment());
					ps.setInt(5, p.getResult().getConfirmed() ? 1 : 0);
					ps.setLong(6, p.getResult().getAdded().getTime() );
					
					ps.executeUpdate();
				}
				
				if(initiatorId != 0){
					
					query = ""
							+ "MATCH (p:Player), (g:Game) "
							+ "WHERE id(p) = ? AND id(g) = ? "
							+ "CREATE (p)-[:Initiated]->(g)";
					
					
					ps = con.prepareStatement(query);
					
					ps.setInt(1, initiatorId);
					ps.setInt(1, gameId);
				}
				
				return gameId;			
			}
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
		
		return 0;
	}

	public static ArrayList<Result> getDeckPlayed(Deck deck) throws NamingException, SQLException {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			String query = ""
					+ "MATCH dp=(d:Deck)<-[r:Evolved*]-() "
					+ "WHERE id(d)=? "
					+ "UNWIND nodes(dp) AS nd "
					+ "WITH nd as deck "
					+ "MATCH (deck)-[:Got]->(r:Result)-[:In]->(:Game) "
					+ "RETURN DISTINCT id(r), PROPERTIES(r)";
			
      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deck.getDeckid());

      		rs = ps.executeQuery();
      		
      		ArrayList<Result> results = new ArrayList<Result>();
			
			while (rs.next()) {
				results.add( new Result( rs.getInt("id(r)"), (Map)rs.getObject("PROPERTIES(r)") ) );
			}

			return results;
			
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
					+ "MATCH dp=(cd:Deck)<-[r:Evolved*0..]-() "
					+ "WHERE id(cd)=? "
					+ "UNWIND nodes(dp) AS nd "
					+ "WITH nd as d "
					+ "MATCH (p:Player)-[:Use|:Used]->(d)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "RETURN DISTINCT id(p), PROPERTIES(p), id(d), PROPERTIES(d), id(r), PROPERTIES(r), id(g), PROPERTIES(g)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, deckId);

      		rs = ps.executeQuery();
      		
      		ArrayList<Participant> participations = new ArrayList<Participant>();
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Result result = new Result( rs.getInt("id(r)"), (Map) rs.getObject("PROPERTIES(r)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				participations.add(new Participant(player, deck, result, game));
			}
			
			return participations;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static ArrayList<Participant> getParticipants(int gameId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (p:Player)-[Use]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) " //TODO Used & Evolved
					+ "WHERE id(g) = ? "
					+ "RETURN id(p), PROPERTIES(p), id(d), PROPERTIES(d), id(r), PROPERTIES(r), id(g), PROPERTIES(g) "
					+ "ORDER BY r.place";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, gameId);

      		rs = ps.executeQuery();
      		
      		ArrayList<Participant> participants = new ArrayList<Participant>();
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Result result = new Result( rs.getInt("id(r)"), (Map) rs.getObject("PROPERTIES(r)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				participants.add(new Participant(player, deck, result, game));
			}
			
			return participants;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static ArrayList<Participant> getUnconfirmedGames(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (p:Player) "
					+ "WHERE id(p) = ? "
					+ "MATCH (p)-[:Use]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE r.confirmed = 0 "
					+ "RETURN id(p), PROPERTIES(p), id(d), PROPERTIES(d), id(r), PROPERTIES(r), id(g), PROPERTIES(g)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		
      		rs = ps.executeQuery();
      		
      		ArrayList<Participant> participants = new ArrayList<Participant>();
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Result result = new Result( rs.getInt("id(r)"), (Map) rs.getObject("PROPERTIES(r)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				participants.add(new Participant(player, deck, result, game));
			}
			
			return participants;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static boolean confirmGame(int resultId, boolean confirm, String comment) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE id(r)=? "
					+ "SET r.confirmed=?, r.comment=?";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, resultId);
			ps.setInt(2, confirm ? 1 : -1);
			ps.setString(3, comment);
			
			ps.executeUpdate();
			
			return true;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}



}




























