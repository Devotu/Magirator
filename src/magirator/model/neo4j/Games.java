package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.google.gson.Gson;

import magirator.dataobjects.Deck;
import magirator.dataobjects.Game;
import magirator.dataobjects.Participant;
import magirator.dataobjects.Player;
import magirator.dataobjects.Result;
import magirator.dataobjects.Tag;
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

			String query = "MATCH (d:Deck)-[:Got]->(r:Result)-[:In]->(:Game) WHERE id(d)=? RETURN id(r), PROPERTIES(r)";

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
					+ "MATCH (p:Player)-[Use]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) " //TODO Used & Evolved
					+ "WHERE id(d) = ? "
					+ "RETURN id(p), PROPERTIES(p), id(d), PROPERTIES(d), id(r), PROPERTIES(r), id(g), PROPERTIES(g) "
					+ "ORDER BY g.created";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, deckId);

      		rs = ps.executeQuery();
      		
      		ArrayList<Participant> participations = new ArrayList<Participant>();
			
			while (rs.next()) {
				Player player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
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
				Player player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
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
      		
      		rs = ps.executeQuery();
      		
      		ArrayList<Participant> participants = new ArrayList<Participant>();
			
			while (rs.next()) {
				Player player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
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

	public static int addTags(ArrayList<Tag> tags, int gameId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = Database.getConnection();
			
			String queryStart = ""
					+ "MATCH (p1:Player), (p2:Player)-[:Use]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE id(p1)=? AND id(p2)=? AND id(g)=? "
					+ "CREATE (p1)-[:Put]->(t:Tag:";
			
			String queryEnd = ""
					+ " {tag: ?})-[:On]->(r)";
			
			int tagsSet = 0;
			
			for (Tag t : tags){
				ps = con.prepareStatement(queryStart + (t.getPolarity() > 0 ? "Positive" : "Negative") + queryEnd); //Inte vackert, går det att lösa på något snyggare sätt?
				
				ps.setInt(1, t.getTagger());
				ps.setInt(2, t.getTagged());
				ps.setInt(3, gameId);
				ps.setString(4, t.getTag());				
				
				tagsSet += (ps.executeUpdate()/3);//Returns 3 for every 1 tag set
			}
			
			return tagsSet;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static List<Tag> getTagsInGame(int gameId) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[]->(g:Game), (tp:Player)-[:Put]->(t:Tag)-[:On]->(r) "
					+ "WHERE id(g)=? "
					+ "RETURN id(tp), PROPERTIES(t), id(r), labels(t)";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, gameId);
      		
      		rs = ps.executeQuery();
      		
      		List<Tag> tags = new ArrayList<Tag>();
			
			while (rs.next()) {
				List<String> labels = (List<String>)rs.getObject("labels(t)");
				Tag t = new Tag(
							rs.getInt("id(tp)"),
							(Map)rs.getObject("PROPERTIES(t)"),
							rs.getInt("id(r)"),
							labels
						);
				
				tags.add(t);
			}
			
			return tags;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}		
	}

}




























