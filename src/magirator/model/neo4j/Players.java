package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import magirator.data.entities.Player;
import magirator.data.entities.User;
import magirator.support.Database;
import magirator.view.PlayerName;

public class Players {

	public static boolean checkIfAvailable(PlayerName requestedPlayer) throws NamingException, SQLException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "MATCH (p:Player) WHERE p.name = ? RETURN p.id";

			ps = con.prepareStatement(query);
			ps.setString(1, requestedPlayer.getPlayerName());

			rs = ps.executeQuery(); ps.close();
			
			if (!rs.next()){ rs.close();
				return true;
			}
			
	        return false;
	        
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	

	public static Player getPlayer(User user) throws NamingException, SQLException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "MATCH (u:User)-[i:Is]->(p:Player) WHERE u.id = ? RETURN PROPERTIES(p)";

			ps = con.prepareStatement(query);
			ps.setInt(1, user.getId());
			
			rs = ps.executeQuery();
		
			if (rs.next()) { //Success
				return new Player( (Map<String, ?>)rs.getObject("PROPERTIES(p)") );
			}
			
	        return null;
	        
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	

	public static List<Player> getAllPlayers(Player player) throws NamingException, SQLException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "MATCH (p:Player) WHERE NOT p.id = ? RETURN PROPERTIES(p)";

			ps = con.prepareStatement(query);
			ps.setInt(1, player.getId());
			
			rs = ps.executeQuery();
			
			List<Player> players = new ArrayList<>();
		
			while (rs.next()) { //Success
				players.add( new Player( (Map<String, ?>)rs.getObject("PROPERTIES(p)") ) );
			}
			
	        return players;
	        
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

}
