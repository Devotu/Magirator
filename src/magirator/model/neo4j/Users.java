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
import magirator.data.entities.Reset;
import magirator.data.entities.User;
import magirator.logic.LoginCredentials;
import magirator.support.Database;
import magirator.view.PlayerName;

public class Users {
	
	public static boolean nameIsAvailable(LoginCredentials requestedUser) throws NamingException, SQLException{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			String query = "MATCH (u:User) WHERE u.name = ? RETURN u.id";

			ps = con.prepareStatement(query);
			ps.setString(1, requestedUser.getUsername());

			rs = ps.executeQuery();
			
			if (!rs.next()){ //Namnet är inte i användning
				return true;
			}
			
	        return false;
	        
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	
	public static boolean signup(LoginCredentials requestedUser, PlayerName requestedPlayer) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();				
			
			String query = ""
					+ "CREATE (u" + User.neoCreator() + ")-[c:Created {created: TIMESTAMP()}]->(p" + Player.neoCreator() + ") "
					+ "CREATE (u)-[i:Is]->(p)"
					+ "RETURN p.id";
			
			ps = con.prepareStatement(query);
			
			List<Object> params = new ArrayList<>();
			params.add(Utility.getUniqueId());
			params.add(requestedUser.getUsername());
			params.add(requestedUser.getPassword());
			params.add(Utility.getUniqueId());
			params.add(requestedPlayer.getPlayerName());
			
			ps = Database.setStatementParams(ps, params);
		
			rs = ps.executeQuery(); ps.close();
			
			if (rs.next()){ rs.close(); //Success
				return true;				
			}
			
			return false;
	        
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
		
	}
	
	public static User login(LoginCredentials loginCredentials) throws NamingException, SQLException{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			con = Database.getConnection();

			String query = ""
					+ "MATCH (u:User) "
					+ "WHERE u.name = ? AND u.password = ? "
					+ "SET u.login = TIMESTAMP() "
					+ "RETURN PROPERTIES(u)";

			ps = con.prepareStatement(query);
      		ps.setString(1, loginCredentials.getUsername());
      		ps.setString(2, loginCredentials.getPassword());

      		rs = ps.executeQuery();
		
			if (rs.next()) { //Användaren finns
				return new User((Map<String, ?>)rs.getObject("PROPERTIES(u)"));
			}
			
			return null;
			
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
		
	}

	/**
	 * Associates a reset code with the user and return its value
	 * If there is a previous reset code that one is overridden
	 * @param loginCredentials
	 * @param code to use for reset
	 * @return reset code or null
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	public static boolean requestResetPassword(LoginCredentials loginCredentials, String code) throws NamingException, SQLException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			con = Database.getConnection();

			String query = ""
					+ "MATCH (u:User) "
					+ "WHERE u.name = ? "
					+ "MERGE (u)-[:Requested]->(rs:Reset) "
					+ "SET rs.code = ?, rs.created = TIMESTAMP() "
					+ "RETURN rs";

			ps = con.prepareStatement(query);
      		ps.setString(1, loginCredentials.getUsername());
      		ps.setString(2, code);

      		rs = ps.executeQuery();
		
			if (rs.next()) { //Reset skapad
				return true;
			}
			
			return false;
			
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	
	/**
	 * Gets the Reset associated with the player if such exist
	 * @param loginCredentials
	 * @return Reset or null
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	public static Reset getUserReset(LoginCredentials loginCredentials) throws NamingException, SQLException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			con = Database.getConnection();

			String query = "MATCH (u:User)-[:Requested]->(rs:Reset) WHERE u.name = ? RETURN PROPERTIES(rs)";

			ps = con.prepareStatement(query);
      		ps.setString(1, loginCredentials.getUsername());

      		rs = ps.executeQuery();
		
			if (rs.next()) { //Reset skapad
				return new Reset((Map<String, ?>)rs.getObject("PROPERTIES(rs)"));
			}
			
			return null;
			
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	/**
	 * Sets a new password granted that there is a matching reset code in the database
	 * @param loginCredentials
	 * @return success
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	public static boolean setPassword(LoginCredentials loginCredentials) throws SQLException, NamingException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			con = Database.getConnection();

			String query = "MATCH (u:User) WHERE u.name = ? SET u.password = ? RETURN u.password";

			ps = con.prepareStatement(query);
      		ps.setString(1, loginCredentials.getUsername());
      		ps.setString(2, loginCredentials.getPassword());

      		rs = ps.executeQuery();
		
			if (rs.next()) { //Reset skapad
				String pwd = rs.getString("u.password");
				return loginCredentials.getPassword().equals(pwd);
			}
			
			return false;
			
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	/**
	 * Clears any resets currently requested by the user (should never be able to have more than one though)
	 * @param loginCredentials
	 * @return removed
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	public static boolean clearReset(LoginCredentials loginCredentials) throws NamingException, SQLException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			con = Database.getConnection();

			String query = "MATCH (u:User)-[:Requested]->(rs:Reset) WHERE u.name = ? DETACH DELETE rs";

			ps = con.prepareStatement(query);
      		ps.setString(1, loginCredentials.getUsername());

      		ps.executeQuery();
      		
			query = "MATCH (u:User)-[:Requested]->(rs:Reset) WHERE u.name = ? RETURN rs";

			ps = con.prepareStatement(query);
      		ps.setString(1, loginCredentials.getUsername());

      		rs = ps.executeQuery();
		
			if (rs.next()) { //Reset still present
				return false;
			}
			
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
		
		return true;
	}

	public static User getUser(Player player) throws SQLException, NamingException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			con = Database.getConnection();

			String query = "MATCH (u:User)-[:Is]->(p:Player) WHERE id(p) = ? RETURN PROPERTIES(u)";

			ps = con.prepareStatement(query);
      		ps.setInt(1, player.getId());

      		rs = ps.executeQuery();
		
			if (rs.next()) { 
				return new User( (Map<String, ?>)rs.getObject("PROPERTIES(u)") );
			}
			
			return null;
			
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

}







