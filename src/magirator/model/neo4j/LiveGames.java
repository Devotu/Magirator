package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

	public static boolean startNewGame(int deckId) throws Exception {

		ResultSet rs = null;
		
		try {	
						
			String query = ""
					+ "MATCH (d:Deck) "
					+ "WHERE d.id = ? "
					+ "CREATE "
					+ "(d)"
					+ "-[:Got]->"
					+ "(:Result {place: 0, comment: '', confirmed: false, added: TIMESTAMP() })"
					+ "-[:In]->"
					+ "(:Game {created: TIMESTAMP(), draw: false})"
					+ "-[:Runs]->"
					+ "(l:Live {token: ?}) "
					+ "RETURN l";
			
			
			List<Object> params = new ArrayList<>();
			params.add(deckId);
			params.add(Encryption.generateLiveGameId());
			
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

}
