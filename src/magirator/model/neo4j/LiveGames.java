package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import magirator.data.collections.PlayerStatus;
import magirator.data.entities.Death;
import magirator.data.entities.Game;
import magirator.data.entities.Life;
import magirator.data.entities.Result;
import magirator.servlets.GetUnconfirmed;
import magirator.support.Constants;
import magirator.support.Database;
import magirator.support.Encryption;

public class LiveGames {
	
	/**
	 * Att klippa klistra från tills vidare
	 * @param playerId
	 * @return
	 * @throws Exception
	 */
	public static String defaultMethod(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "";
			
			List<Object> params = new ArrayList<>();
			params.add(playerId);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				return rs.getString("");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}		
	}
	
	public static boolean isPlayerInGame(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (p:Player:InGame) "
					+ "WHERE p.id = ? "
					+ "RETURN p";
			
			List<Object> params = new ArrayList<>();
			params.add(playerId);
			
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	

	public static String startNewGame(int deckId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
						
			String query = ""
					+ "MATCH (p:Player)-[:Use]->(d:Deck) "
					+ "WHERE d.id = ? "
					+ "CREATE "
					+ "(d)"
					+ "-[:Got]->"
					+ "(r"+ Result.neoCreator() +")"
					+ "-[:In]->"
					+ "(:Live"+ Game.neoCreator() +"), "
					+ "(r)-[:StartedWith]->("+ Life.neoCreator() +")"
					+ "SET p:InGame, p:GameAdmin, p.live_token = ? "
					+ "RETURN p.live_token AS token";
			
			List<Object> params = new ArrayList<>();
			params.add(deckId);
			params.add(Utility.getUniqueId());
			params.add(0);
			params.add("");
			params.add(Utility.getUniqueId());
			params.add(false);
			params.add(Encryption.generateLiveGameId()); //TODO verify uniqeness
			params.add(Utility.getUniqueId());
			params.add(Constants.startingLifeStandard);
			params.add(Encryption.generateLiveToken()); //TODO verify uniqeness
			
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				return rs.getString("token");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	
	public static String joinGame(int deckId, String liveId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
						
			String query = ""
					+ "MATCH (g:Game), (p:Player)-[:Use]->(d:Deck) "
					+ "WHERE g.live_id = ? AND d.id = ? "
					+ "CREATE "
					+ "(d)"
					+ "-[:Got]->"
					+ "(r"+ Result.neoCreator() +")"
					+ "-[:In]->"
					+ "(g), "
					+ "(r)-[:StartedWith]->("+ Life.neoCreator() +")"
					+ "SET p:InGame, p.live_token = ? "
					+ "RETURN p.live_token AS token";
			
			List<Object> params = new ArrayList<>();
			params.add(liveId);
			params.add(deckId);
			params.add(Utility.getUniqueId());
			params.add(0);
			params.add("");
			params.add(Utility.getUniqueId());
			params.add(Constants.startingLifeStandard);
			params.add(Encryption.generateLiveToken());
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				return rs.getString("token");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	
	public static String getPlayerLiveGameToken(int playerId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game:Live) "
					+ "WHERE p.id = ? "
					+ "RETURN p.live_token AS token";
			
			List<Object> params = new ArrayList<>();
			params.add(playerId);
			
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				return rs.getString("token");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	
	public static String getPlayerLiveId(String token) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game:Live) "
					+ "WHERE p.live_token = ? "
					+ "RETURN g.live_id AS live_id";
			
			List<Object> params = new ArrayList<>();
			params.add(token);
			
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				return rs.getString("live_id");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	
	public static boolean changeLife(String liveId, String reporterToken, String updatedToken, int newLife) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (rp:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game) "
					+ "WHERE rp.live_token = ? AND g.live_id = ? "
					+ "MATCH (up)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE up.live_token = ? AND g.live_id = ? "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(l:Life) "
					+ "WHERE NOT (l)-->() AND length(lifelog) > 0 "
					+ "WITH LAST(NODES(lifelog)[1..]) AS lastLife "
					+ "CREATE (lastLife)-[:ChangedTo]->(newLife"+ Life.neoCreator() +") "
					+ "RETURN newLife";
			
			List<Object> params = new ArrayList<>();
			params.add(reporterToken);
			params.add(liveId);
			params.add(updatedToken);
			params.add(liveId);
			params.add(Utility.getUniqueId());
			params.add(newLife);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	
	public static String getGameStatusAsJson(String liveId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement st = null;
		
		try {
			
			String query = ""
					+ "MATCH (g:Game)<-[:In]-(r:Result)<-[:Got]-(d:Deck)<-[:Use|:Used]-(p) "
					+ "WHERE g.live_id=? "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(life:Life) "
					+ "WHERE NOT (life)-->() OR (life)-->(:Death) AND length(lifelog) > 0 "
					+ "WITH p,d,r,lifelog, LAST(NODES(lifelog)[1..]) AS currentLife "
					+ "OPTIONAL MATCH (currentLife)-[ChangedTo]->(death:Death) "
					+ "RETURN { "
					+ "		checksum:  sum(length(lifelog)) + count(death), "
					+ "		participants: collect(	{"
					+ "			player_name: p.name, "
					+ "			player_token: p.live_token, "
					+ "			life: currentLife.life, "
					+ "			dead: NOT death IS NULL, "
					+ "			place: r.place"
					+ "			} "
					+ "		), "
					+ "		next_death:count(p)-count(death) "
					+ "} as status";
			
			List<Object> params = new ArrayList<>();
			params.add(liveId);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			rs = ps.executeQuery();
			
			if(rs.next()){
				Map pMap = (Map) rs.getObject("status");
				String pJson = new Gson().toJson(pMap, Map.class);
				return pJson;
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}		
	}
	
	public static String getGameStatusAsJson(String liveId, String token) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement st = null;
		
		try {
			
			String query = ""
					+ "MATCH (g:Game)<-[:In]-(r:Result)<-[:Got]-(d:Deck)<-[:Use|:Used]-(p:Player) "
					+ "WHERE g.live_id=? "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(life:Life) "
					+ "WHERE NOT (life)-->() OR (life)-->(:Death) AND length(lifelog) > 0 "
					+ "WITH p,d,r,lifelog, LAST(NODES(lifelog)[1..]) AS currentLife, p.live_token=? AS self "
					+ "OPTIONAL MATCH (currentLife)-[ChangedTo]->(death:Death) "
					+ "RETURN { "
					+ "		checksum:  sum(length(lifelog)) + count(death), "
					+ "		participants: collect(	{"
					+ "			player_name: p.name, "
					+ "			player_token: p.live_token, "
					+ "			life: currentLife.life, "
					+ "			dead: NOT death IS NULL, "
					+ "			place: r.place, "
					+ "			self: self"
					+ "			} "
					+ "		), "
					+ "		next_death:count(p)-count(death) "
					+ "} as status";
			
			List<Object> params = new ArrayList<>();
			params.add(liveId);
			params.add(token);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			rs = ps.executeQuery();
			
			if(rs.next()){
				Map pMap = (Map) rs.getObject("status");
				String pJson = new Gson().toJson(pMap, Map.class);
				return pJson;
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	
	//Leave Game
	//The player is removed and if it is the last player the game is deleted
	public static String leaveGame(String token) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game:Live), "
					+ "(r)-[:StartedWith|:ChangedTo*0..]->(life:Life) "
					+ "WHERE p.live_token = ? "
					+ "DETACH DELETE r,life"; //TODO Game if last player //TODO remove player token (and admin)
			
			List<Object> params = new ArrayList<>();
			params.add(token);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				return rs.getString("");
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}		
	}

	public static boolean isAdmin(String token) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String query = ""
					+ "MATCH (p:Player:GameAdmin) "
					+ "WHERE p.live_token = ? RETURN p";
			
			List<Object> params = new ArrayList<>();
			params.add(token);
			
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			return rs.next();
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}	
	
	//End Game (migth be)
	
	/**
	 * All participants are removed and the game is deleted
	 * @param liveId
	 * @return true if the game is removed otherwise false
	 * @throws Exception
	 */
	public static boolean cancelGame(String liveId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (p:Player:InGame)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game:Live), "
					+ "(r)-[:StartedWith|:ChangedTo*]->(l:Life) "
					+ "OPTIONAL MATCH "
					+ "(gap:Player:GameAdmin)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game) "
					+ "OPTIONAL MATCH "
					+ "(currentLife)-[ChangedTo]->(d:Death) "
					+ "WHERE g.live_id = ? "
					+ "REMOVE p:InGame, p.live_token, gap:GameAdmin "
					+ "DETACH DELETE g,r,l,d";
			
			List<Object> params = new ArrayList<>();
			params.add(liveId);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			return true;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}		
	}
	
	/**
	 * @param liveId
	 * @param token
	 * @return place in game
	 * @throws Exception
	 */
	public static int declareDead(String liveId, String token, int place) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			
			String query = ""
					+ "MATCH (p:Player:InGame)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game:Live) "
					+ "WHERE p.live_token = ? AND g.live_id = ? "
					+ "MATCH lifelog=(r)-[:StartedWith|:ChangedTo*0..]->(l:Life) "
					+ "WHERE NOT (l)-->() AND length(lifelog) > 0 "
					+ "WITH LAST(NODES(lifelog)[1..]) AS lastLife, r "
					+ "CREATE (lastLife)-[:ChangedTo]->(self_death" + Death.neoCreator() + ") "
					+ "SET r.place=? "
					+ "RETURN r.place";
			
			List<Object> params = new ArrayList<>();
			params.add(token);
			params.add(liveId);
			params.add(Utility.getUniqueId());
			params.add(place);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				return rs.getInt("r.place");
			}
			
			return 0;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}

	public static boolean declareWinner(String liveId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			
			//TODO Verification single player left
			String query = ""
					+ "MATCH (g:Game:Live)<-[:In]-(r:Result) "
					+ "WHERE g.live_id=? AND r.place=0 "
					+ "SET r.place=1 "
					+ "RETURN r";
			
			List<Object> params = new ArrayList<>();
			params.add(liveId);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	

	public static boolean endGame(String liveId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			
			//TODO Verification no players left
			String query = ""
					+ "MATCH (p:Player:InGame)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game:Live) "
					+ "OPTIONAL MATCH "
					+ "(gap:Player:InGame:GameAdmin)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game:Live) "
					+ "WHERE g.live_id = ? "
					+ "REMOVE p:InGame, gap:GameAdmin, g:Live "
					+ "RETURN g";
			
			List<Object> params = new ArrayList<>();
			params.add(liveId);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}

}
