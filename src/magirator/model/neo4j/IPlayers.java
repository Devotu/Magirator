package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import magirator.data.collections.IPlayerGame;
import magirator.data.entities.Game;
import magirator.data.entities.Minion;
import magirator.data.entities.Player;
import magirator.data.interfaces.IPlayer;
import magirator.support.Database;

public class IPlayers {

	public static IPlayer getIPlayer(int playerId) throws NamingException, SQLException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();
			
			
			String query = ""
					+ "MATCH (p:Player) "
					+ "WHERE id(p) = ? WITH collect(p) as p1 "
					+ "OPTIONAL MATCH (m:Minion) "
					+ "WHERE id(m) = ? WITH collect(m) + p1 as p2 "
					+ "UNWIND p2 as player "
					+ "RETURN id(player), PROPERTIES(player)";

			ps = con.prepareStatement(query);
			ps.setInt(1, playerId);
			ps.setInt(2, playerId);
			
			rs = ps.executeQuery();
		
			if (rs.next()) { //Success
				return new Player( rs.getInt("id(player)"), (Map)rs.getObject("PROPERTIES(player)") );
			}
			
	        return null;
	        
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	
	
	public static List<IPlayerGame> getPlayerPreviousOpponents(IPlayer player) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();
	
			String query = ""
					+ "MATCH (s:Player)-[:Use|:Used]->(:Deck)-[:Got]->(:Result)-[:In]->(g:Game)<-[:In]-(:Result)<-[:Got]-(:Deck)<-[:Use|:Used]-(p:Player) "
					+ "WHERE id(s) = ? "
					+ "RETURN id(p), PROPERTIES(p), id(g), PROPERTIES(g)";
	
	  		ps = con.prepareStatement(query);
	  		ps.setInt(1, player.getId());
	
	  		rs = ps.executeQuery();
	  		
	  		List<IPlayerGame> previous = new ArrayList<>();
			
			while (rs.next()) {
				IPlayer p = new Player( rs.getInt("id(p)"), (Map)rs.getObject("PROPERTIES(p)") );
				Game g = new Game( rs.getInt("id(g)"), (Map)rs.getObject("PROPERTIES(g)") );
				previous.add( new IPlayerGame(p, g) );
			}
	
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return previous;
			
		} catch (Exception ex){
			throw ex;
		}		
	}
}
