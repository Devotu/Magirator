package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import magirator.data.collections.GameBundle;
import magirator.data.collections.Participant;
import magirator.data.collections.PlayerDeck;
import magirator.data.entities.Deck;
import magirator.data.entities.Game;
import magirator.data.entities.Player;
import magirator.data.entities.Rating;
import magirator.data.entities.Result;
import magirator.data.interfaces.IPlayer;
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
					+ "MATCH (deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE NOT (g)-[:Runs]->(:Live) "
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
					+ "WHERE NOT (g)-[:Runs]->(:Live) "
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
					+ "MATCH (p:Player)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) " //TODO Used & Evolved
					+ "WHERE id(g) = ? "
					+ "OPTIONAL MATCH (p)-[:Gave]->(rt)-[to]->(r) "
					+ "RETURN id(p), PROPERTIES(p), id(d), PROPERTIES(d), id(r), PROPERTIES(r), id(rt), PROPERTIES(rt), id(g), PROPERTIES(g) "
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
				

				
				Participant participant = new Participant(player, deck, result, game);
				
				rs.getMetaData();
				
				if (rs.getObject("id(rt)") != null) {
					Rating rating = new Rating(rs.getInt("id(rt)"), (Map) rs.getObject("PROPERTIES(rt)"));
					participant.setRating(rating);
				}
				
				participants.add(participant);				
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

	public static ArrayList<Participant> getUnconfirmedParticipations(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (p:Player) "
					+ "WHERE id(p) = ? "
					+ "MATCH (p)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
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

	public static boolean confirmGame(int resultId, boolean confirm, String comment, Rating rating) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE id(r)=? "
					+ "SET r.confirmed=?, r.comment=?";
			
			if (rating != null){
				query += " CREATE (p)-[:Gave]->(rt:Rating {speed:?, strength:?, synergy:?, control:?})-[:To]->(r)";
			}
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, resultId);
			ps.setInt(2, confirm ? 1 : -1);
			ps.setString(3, comment);
			
			if (rating != null){
				ps.setInt(4, rating.getSpeed());
				ps.setInt(5, rating.getStrength());
				ps.setInt(6, rating.getSynergy());
				ps.setInt(7, rating.getControl());
			}
			
			ps.executeUpdate();
			
			return true;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static List<GameBundle> getPlayerGames(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (player:Player)"
					+ "WHERE id(player) = ?"
					+ "MATCH (player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)"
					+ "MATCH (pm)-[:Use]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g)"
					+ "RETURN id(pm), PROPERTIES(pm), id(d), PROPERTIES(d), id(r), PROPERTIES(r), id(g), PROPERTIES(g)"
					+ "ORDER BY id(g)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		
      		rs = ps.executeQuery();
      		
      		List<GameBundle> games = new ArrayList<>();      		
      		GameBundle gb = null;
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(pm)"), (Map) rs.getObject("PROPERTIES(pm)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Result result = new Result( rs.getInt("id(r)"), (Map) rs.getObject("PROPERTIES(r)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				Participant p = new Participant(player, deck, result, game);
				
				if (gb == null) {
					gb = new GameBundle(game);
				}
								
				if (!gb.isSameGame(p)) {
					games.add(gb);
					gb = new GameBundle(game);
				}
				
				if (player.getId() == playerId) {
					gb.addSelf(p);
				} else {
					gb.addOpponent(p);
				}
			}
			
			games.add(gb);
			
			return games;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	
	public static int startGame(ArrayList<PlayerDeck> participants, int initiatorId, String gameToken) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "CREATE (g:Game {created: TIMESTAMP()})-[:Runs]->(l:Live {token: ?}) RETURN id(g)";
			
			ps = con.prepareStatement(query);

			ps.setString(1, gameToken);
			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				int gameId = rs.getInt("id(g)");
				
				query = ""
						+ "MATCH (g:Game), (d:Deck) "
						+ "WHERE id(g) = ? AND id(d) = ? "
						+ "CREATE (d)-[:Got]->(r:Result {place: 0, comment: '', confirmed: false, added: TIMESTAMP() })-[:In]->(g)";
				
				ps = con.prepareStatement(query);
				
				for (PlayerDeck p : participants){
					ps.setInt(1, gameId);
					ps.setInt(2, p.getDeck().getDeckid());
					
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


	public static String getPlayerLiveGameToken(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (p:Player) "
					+ "WHERE id(p) = ? "
					+ "MATCH (p)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game)-[:Runs]->(l:Live) "
					+ "RETURN l.token";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		
      		rs = ps.executeQuery();
      		
      		String token = "none";
      					
			if (rs.next()) {
				token = rs.getString("l.token");
			}
			
			return token;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	
	public static List<PlayerDeck> getPlayerLiveGame(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (self:Player) "
					+ "WHERE id(self) = ? "
					+ "MATCH (self)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)-[:Runs]->(l:Live) "
					+ "MATCH (p:Player)-[:Use|:Used]->(d:Deck)-[:Got]->(:Result)-[:In]->(g:Game) "
					+ "RETURN id(p), PROPERTIES(p), id(d), PROPERTIES(d)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		
      		rs = ps.executeQuery();
      		
      		List<PlayerDeck> participants = new ArrayList<>();
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				
				participants.add( new PlayerDeck( player, deck) );
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
	
	
	public static int confirmLiveGame(int PlayerId, String comment, Rating rating, List<Integer> lifelog) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game)-[:Runs]->(l:Live) "
					+ "WHERE id(p)=? "
					+ "SET r.confirmed=1, r.comment=?";
			
			if (rating != null){
				query += " CREATE (p)-[:Gave]->(rt:Rating {speed:?, strength:?, synergy:?, control:?})-[:To]->(r)";
			}
			
			query += "RETURN id(g)";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, PlayerId);
			ps.setString(2, comment);
			
			if (rating != null){
				ps.setInt(3, rating.getSpeed());
				ps.setInt(4, rating.getStrength());
				ps.setInt(5, rating.getSynergy());
				ps.setInt(6, rating.getControl());
			}
			
      		rs = ps.executeQuery();
			
			if (rs.next()) {
				return rs.getInt("id(g)");
			}
			
			return 0;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

}




























