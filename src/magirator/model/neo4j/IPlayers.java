package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.NamingException;

import magirator.data.interfaces.IPlayer;
import magirator.data.objects.Player;
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
	
public static ArrayList<IPlayer> getOpponents(IPlayer player) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			//TODO Get more specific
			String query = "MATCH (p:Player)" +
				"WHERE NOT id(p) = ?" +
				"MATCH (p)-->(d:Deck)" +
				"RETURN DISTINCT id(p), PROPERTIES(p)";

      		ps = con.prepareStatement(query);
      		ps.setInt(1, player.getId());

      		rs = ps.executeQuery();
      		
      		ArrayList<IPlayer> opponents = new ArrayList<IPlayer>();
			
			while (rs.next()) {
				opponents.add( new Player( rs.getInt("id(p)"), (Map)rs.getObject("PROPERTIES(p)") ) );
			}

			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return opponents;
			
		} catch (Exception ex){
			throw ex;
		}		
	}
}
