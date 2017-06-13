package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import magirator.data.collections.PlayerStatus;
import magirator.support.Database;
import magirator.support.Encryption;

public class LiveGames {
	
	public static ResultSet runQuery(String query, List<Object> params) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			
			ps = con.prepareStatement(query);
			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			return rs;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	
	
	public static boolean isPlayerInGame(int playerId) throws Exception {

		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (p:Player:InGame) "
					+ "WHERE p.id = ? "
					+ "RETURN p";
			
			List<Object> params = new ArrayList<>();
			params.add(playerId);
			
			rs = runQuery(query, params);
			
			if	(rs.next()){
				
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
		}
	}
	

	public static String startNewGame(int deckId) throws Exception {

		ResultSet rs = null;
		
		try {
			int gameId = Utility.getUniqueId();
						
			String query = ""
					+ "MATCH (p:Player)-[:Use]->(d:Deck) "
					+ "WHERE d.id = ? "
					+ "CREATE "
					+ "(d)"
					+ "-[:Got]->"
					+ "(:Result {place: 0, comment: '', confirmed: false, added: TIMESTAMP() })"
					+ "-[:In]->"
					+ "(:Game:Live {id: ?, created: TIMESTAMP(), draw: false, live_id: ?}) "
					+ "SET p:InGame, p:GameAdmin, p.live_token = ? "
					+ "RETURN p.live_token AS token";
			
			List<Object> params = new ArrayList<>();
			params.add(deckId);
			params.add(gameId);
			params.add(Encryption.generateLiveGameId());
			params.add(Encryption.generateLiveToken());
			
			rs = runQuery(query, params);
			
			if	(rs.next()){
				
				return rs.getString("token");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
		}
	}
	
	
	public static String joinGame(int deckId, String liveId) throws Exception {

		ResultSet rs = null;
		
		try {	
						
			String query = ""
					+ "MATCH (g:Game), (p:Player)-[:Use]->(d:Deck) "
					+ "WHERE g.live_id = ? AND d.id = ? "
					+ "CREATE "
					+ "(d)"
					+ "-[:Got]->"
					+ "(r:Result {place: 0, comment: '', confirmed: false, added: TIMESTAMP() })"
					+ "-[:In]->"
					+ "(g) "
					+ "SET p:InGame, p.live_token = ? "
					+ "RETURN p.live_token AS token";
			
			List<Object> params = new ArrayList<>();
			params.add(liveId);
			params.add(deckId);
			params.add(Encryption.generateLiveToken());
						
			rs = runQuery(query, params);
			
			if	(rs.next()){
				
				return rs.getString("token");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
		}
	}
	
	
	public static String getPlayerLiveId(String token) throws Exception {

		ResultSet rs = null;
		
		try {
			int gameId = Utility.getUniqueId();
						
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game) "
					+ "WHERE p.live_token = ? "
					+ "RETURN g.live_id AS live_id";
			
			List<Object> params = new ArrayList<>();
			params.add(token);
			
			rs = runQuery(query, params);
			
			if	(rs.next()){
				
				return rs.getString("live_id");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
		}
	}
	
	
	public static boolean changeLife(String liveId, String token, int playerId, int alterAmount) throws Exception {

		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (rp:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game) "
					+ "WHERE rp.live_token = ? AND g.live_id = ? "
					+ "MATCH (up)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE up.id = ? AND g.live_id = ? "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(l:Life) "
					+ "WHERE NOT (l)-->() AND length(lifelog) > 0 "
					+ "WITH LAST(NODES(lifelog)[1..]) AS lastLife "
					+ "CREATE (lastLife)-[:ChangedTo]->(newLife:Life {added: TIMESTAMP(), life: lastLife + ?}) "
					+ "RETURN newLife";
			
			List<Object> params = new ArrayList<>();
			params.add(token);
			params.add(liveId);
			params.add(playerId);
			params.add(liveId);
			params.add(alterAmount);
						
			rs = runQuery(query, params);
			
			if(rs.next()){
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
		}
	}
	
	
	public static boolean declareDead(String liveId, String token) throws Exception {

		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (p)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE p.live_token = ? AND g.live_id = ? "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(l:Life) "
					+ "WHERE NOT (l)-->() AND length(lifelog) > 0 "
					+ "WITH LAST(NODES(lifelog)[1..]) AS lastLife "
					+ "CREATE (lastLife)-[:ChangedTo]->(death:Death {added: TIMESTAMP()}) "
					+ "SET r.confirmed = true "
					+ "RETURN death";
			
			List<Object> params = new ArrayList<>();
			params.add(token);
			params.add(liveId);
						
			rs = runQuery(query, params);
			
			if(rs.next()){
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
		}
	}
	
	
	public static String getGameStatusAsJson(String liveId, String token, int playerId) throws Exception {

		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (g:Game)<-[:In]-(r:Result)<-[:Got]-(d:Deck)<-[:Use|:Used]-(p) "
					+ "WHERE g.live_id=? "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(life:Life) "
					+ "WHERE NOT (life)-->() AND length(lifelog) > 0 "
					+ "WITH p,d,r, LAST(NODES(lifelog)[1..]) AS currentLife "
					+ "OPTIONAL MATCH (currentLife)-[ChangedTo]->(death:Death) "
					+ "RETURN {"
					+ "	number_of_participants: count(p), "
					+ "	participants: collect({player_id: p.id, deck_id: d.id, life: currentLife.life, dead: death NOT IS NULL, place: r.place, confirmed: r.confirmed})"
					+ "} AS participants";
			
			List<Object> params = new ArrayList<>();
			params.add(liveId);
						
			rs = runQuery(query, params);
			
			if(rs.next()){
				return rs.getString("participants");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
		}		
	}

}
