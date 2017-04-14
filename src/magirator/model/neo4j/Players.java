package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.NamingException;

import magirator.data.objects.Player;
import magirator.data.objects.User;
import magirator.support.Database;
import magirator.viewobjects.PublicPlayer;

public class Players {

	public static boolean checkIfAvailable(PublicPlayer requestedPlayer) throws NamingException, SQLException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "MATCH (p:Player) WHERE p.name = ? RETURN id(p)";

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
			
			String query = "MATCH (u:User)-[i:Is]->(p:Player) WHERE id(u) = ? RETURN id(p), PROPERTIES(p)";

			ps = con.prepareStatement(query);
			ps.setInt(1, user.getId());
			
			rs = ps.executeQuery();
		
			if (rs.next()) { //Success
				return new Player( rs.getInt("id(p)"), (Map)rs.getObject("PROPERTIES(p)") );
			}
			
	        return null;
	        
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

}
