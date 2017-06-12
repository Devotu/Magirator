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

import org.neo4j.jdbc.Array;

import magirator.data.collections.GameBundle;
import magirator.data.collections.Participant;
import magirator.data.collections.PlayerGameResult;
import magirator.data.collections.PlayerStatus;
import magirator.data.collections.PlayerDeck;
import magirator.data.entities.Deck;
import magirator.data.entities.Game;
import magirator.data.entities.Player;
import magirator.data.entities.Rating;
import magirator.data.entities.Result;
import magirator.data.interfaces.IPlayer;
import magirator.support.Database;

public class Games {
	
	public static int addGame(ArrayList<PlayerGameResult> participants, boolean draw, int initiatorId) throws Exception {
		
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
				
				for (PlayerGameResult p : participants){
					ps.setInt(1, gameId);
					ps.setInt(2, p.getDeck().getDeckid());
					ps.setInt(3, p.getResult().getPlace());
					ps.setString(4, p.getResult().getComment());
					ps.setBoolean(5, p.getResult().getConfirmed());
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

	public static ArrayList<PlayerGameResult> getDeckParticipations(int deckId) throws Exception {

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
      		
      		ArrayList<PlayerGameResult> participations = new ArrayList<PlayerGameResult>();
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Result result = new Result( rs.getInt("id(r)"), (Map) rs.getObject("PROPERTIES(r)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				participations.add(new PlayerGameResult(player, deck, result, game));
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

	public static ArrayList<PlayerGameResult> getParticipants(int gameId) throws Exception {

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
      		
      		ArrayList<PlayerGameResult> participants = new ArrayList<PlayerGameResult>();
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Result result = new Result( rs.getInt("id(r)"), (Map) rs.getObject("PROPERTIES(r)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				

				
				PlayerGameResult participant = new PlayerGameResult(player, deck, result, game);
				
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

	public static ArrayList<PlayerGameResult> getUnconfirmedParticipations(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (p:Player) "
					+ "WHERE id(p) = ? "
					+ "MATCH (p)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE r.confirmed = false "
					+ "RETURN id(p), PROPERTIES(p), id(d), PROPERTIES(d), id(r), PROPERTIES(r), id(g), PROPERTIES(g)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		
      		rs = ps.executeQuery();
      		
      		ArrayList<PlayerGameResult> participants = new ArrayList<PlayerGameResult>();
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				Result result = new Result( rs.getInt("id(r)"), (Map) rs.getObject("PROPERTIES(r)") );
				Game game = new Game( rs.getInt("id(g)"), (Map) rs.getObject("PROPERTIES(g)") );
				
				participants.add(new PlayerGameResult(player, deck, result, game));
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
			ps.setBoolean(2, confirm);
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
				
				PlayerGameResult p = new PlayerGameResult(player, deck, result, game);
				
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

	
	public static int startGame(ArrayList<PlayerDeck> participants, int initiatorId, String gameToken, int initialLife) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "CREATE (g:Game {created: TIMESTAMP(), draw: false})-[:Runs]->(l:Live {token: ?}) RETURN id(g)";
			
			ps = con.prepareStatement(query);

			ps.setString(1, gameToken);
			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				int gameId = rs.getInt("id(g)");
				
				query = ""
						+ "MATCH (g:Game), (d:Deck) "
						+ "WHERE id(g) = ? AND id(d) = ? "
						+ "CREATE (d)-[:Got]->(r:Result {place: 0, comment: '', confirmed: false, added: TIMESTAMP() })-[:In]->(g), (r)-[:StartedWith]->(:Life {life:?})";
				
				ps = con.prepareStatement(query);
				
				for (PlayerDeck p : participants){
					ps.setInt(1, gameId);
					ps.setInt(2, p.getDeck().getDeckid());
					ps.setInt(3, initialLife);
					
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
					+ "MATCH (p)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(:Game)-[:Runs]->(l:Live) "
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
	
	public static List<Participant> getPlayerLiveGameParticipants(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (self:Player) "
					+ "WHERE id(self) = ? "
					+ "MATCH (self)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)-[:Runs]->(:Live) "
					+ "MATCH (p)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(l:Life) "
					+ "WHERE NOT (l)-->() AND length(lifelog) > 0 "
					+ "WITH p,d,r, LAST(NODES(lifelog)[1..]) AS cl "
					+ "RETURN id(p), PROPERTIES(p), id(d), PROPERTIES(d), cl.life, p:Minion, r.confirmed, r.place";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		
      		rs = ps.executeQuery();
      		
      		List<Participant> participants = new ArrayList<>();
			
			while (rs.next()) {
				IPlayer player = new Player( rs.getInt("id(p)"), (Map) rs.getObject("PROPERTIES(p)") );
				Deck deck = new Deck( rs.getInt("id(d)"), (Map) rs.getObject("PROPERTIES(d)") );
				int life = rs.getInt("cl.life");
				boolean isMinon = rs.getBoolean("p:Minion");
				boolean isConfirmed = rs.getBoolean("r.confirmed");
				int place = rs.getInt("r.place");
				
				participants.add( new Participant( player, deck, life, isMinon, isConfirmed, place) );
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
	
	
	public static boolean updateLivePlayerLife(int updatedPlayerId, int reportingPlayerId, int newLife, long time) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (rp:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)-[:Runs]->(:Live) "
					+ "WHERE id(rp)=? "
					+ "MATCH (up)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE id(up)=? "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(l:Life) "
					+ "WHERE NOT (l)-->() AND length(lifelog) > 0 "
					+ "WITH LAST(NODES(lifelog)[1..]) AS lastLife "
					+ "CREATE (lastLife)-[:ChangedTo]->(newLife:Life {time:?, life:?}) "
					+ "RETURN lastLife, newLife";
			
			ps = con.prepareStatement(query);

			ps.setInt(1, reportingPlayerId);
			ps.setInt(2, updatedPlayerId);
			ps.setLong(3, time);
			ps.setInt(4, newLife);
			
			int result = ps.executeUpdate();
			
			return result != 0;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	
	
	public static boolean updateLiveGameAttributes(int playerId, boolean draw) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game)-[:Runs]->(l:Live) "
					+ "WHERE id(p)=? "
					+ "SET g.draw=?"
					+ "RETURN g.draw";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, playerId);
			ps.setBoolean(2, draw);
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				return (rs.getBoolean("g.draw") == draw);
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	
	
	public static int confirmLiveGame(int playerId, int place, String comment, Rating rating) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (p)-[:Use|:Used]->(d:Deck)-[:Got]->(r:Result)-[:In]->(g:Game)-[:Runs]->(l:Live) "
					+ "WHERE id(p)=? "
					+ "SET r.confirmed=true, r.place=?, r.comment=?";
			
			if (rating != null){
				query += " CREATE (p)-[:Gave]->(rt:Rating {speed:?, strength:?, synergy:?, control:?})-[:To]->(r) ";
			}
			
			query += "RETURN id(g)";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, playerId);
			ps.setInt(2, place);
			ps.setString(3, comment);
			
			if (rating != null){
				ps.setInt(4, rating.getSpeed());
				ps.setInt(5, rating.getStrength());
				ps.setInt(6, rating.getSynergy());
				ps.setInt(7, rating.getControl());
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
	
	public static boolean endLiveGame(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement queryPS = null;
		PreparedStatement deletePS = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)-[:Runs]->(l:Live), (r:Result)-[:In]->(g) "
					+ "WHERE id(p)=? AND NOT r.confirmed=true "
					+ "RETURN id(r)";
			
			queryPS = con.prepareStatement(query);
      		
			queryPS.setInt(1, playerId);
			
      		rs = queryPS.executeQuery();
			
			if (rs.next()) {
				return false;
			}
			
			String deleteQuery = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)-[:Runs]->(l:Live)"
					+ "WHERE id(p)=? "
					+ "DETACH DELETE l";
			
			deletePS = con.prepareStatement(deleteQuery);
      		
			deletePS.setInt(1, playerId);
			
			int result = deletePS.executeUpdate();
			
			return result != 0;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (queryPS != null) queryPS.close();
			if (deletePS != null) deletePS.close();
			if (con != null) con.close();
		}
	}

	public static boolean abortLiveGame(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)-[:Runs]->(l:Live) "
					+ "WHERE id(p)=? "
					+ "MATCH (g)--(x) "
					+ "DETACH DELETE g, x";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, playerId);
			
			int result = ps.executeUpdate();
			
			return result != 0;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static List<PlayerStatus> getGameStatus(String token) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (liveGame:Live)<-[:Runs]-(:Game)<-[:In]-(result:Result)<-[:Got]-(:Deck)<-[:Use|:Used]-(player) "
					+ "WHERE liveGame.token=? "
					+ "MATCH lifelog=(result)-[:StartedWith|:ChangedTo*0..]->(life:Life) "
					+ "WHERE NOT (life)-->() AND length(lifelog) > 0 "
					+ "WITH player, result, LAST(NODES(lifelog)[1..]) AS currentLife "
					+ "RETURN id(player), currentLife.life, result.confirmed, result.place";

      		ps = con.prepareStatement(query);
      		ps.setString(1, token);
      		
      		rs = ps.executeQuery();
      		
      		List<PlayerStatus> statuses = new ArrayList<>();
			
			while (rs.next()) {
				statuses.add( new PlayerStatus( rs.getInt("id(player)"), rs.getInt("currentLife.life"), rs.getBoolean("result.confirmed"), rs.getInt("result.place")) );
			}
			
			return statuses;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static int getPlayerLiveGameId(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (self:Player) "
					+ "WHERE id(self) = ? "
					+ "MATCH (self)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)-[:Runs]->(:Live) "
					+ "RETURN id(g)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		
      		rs = ps.executeQuery();
      		
      		List<Participant> participants = new ArrayList<>();
			
			if (rs.next()) {
				return rs.getInt("id(g)");
			}
			
			return 0;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static boolean gameIsDraw(int gameId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (g:Game) "
					+ "WHERE id(g) = ?"
					+ "RETURN g.draw";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, gameId);
      		
      		rs = ps.executeQuery();
      					
			if (rs.next()) {
				return rs.getBoolean("g.draw");
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static int getPlaceInGame(int gameId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (r:Result)-[:In]->(g:Game) "
					+ "WHERE id(g) = ? "
					+ "RETURN count(r)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, gameId);
      		
      		rs = ps.executeQuery();
      		
      		if(rs.next()){
      			
      			int numberOfParticipants = rs.getInt("count(r)");
      			
      			query = ""
    					+ "MATCH (rc:Result)-[:In]->(g:Game) "
    					+ "WHERE id(g) = ? AND rc.confirmed = true "
    					+ "RETURN count(rc)";
      			
      			ps = con.prepareStatement(query);
          		ps.setInt(1, gameId);
          		
          		rs = ps.executeQuery();
          		
          		if(rs.next()){
          			int numberOfConfirmed = rs.getInt("count(rc)");
          			
          			return numberOfParticipants - numberOfConfirmed;
          		}
          		
          		return numberOfParticipants;
      			
      		}
      		
      		return -1;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static int placeInGame(int playerId, int gameId) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MATCH (p)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE id(p)=? AND id(g)=? "
					+ "RETURN r.place";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, playerId);
      		ps.setInt(2, gameId);
      		
      		rs = ps.executeQuery();
      		
      		if(rs.next()){
      			
      			return rs.getInt("r.place");      			
      		}
      		
      		return 0;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

}




























