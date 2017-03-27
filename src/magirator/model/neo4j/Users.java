package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.NamingException;
import magirator.dataobjects.User;
import magirator.support.Database;
import magirator.viewobjects.LoginCredentials;
import magirator.viewobjects.PublicPlayer;

public class Users {
	
	public static boolean checkIfAvailable(LoginCredentials requestedUser) throws NamingException, SQLException{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			String query = "MATCH (n:User) WHERE n.name = ? RETURN id(n)";

			ps = con.prepareStatement(query);
			ps.setString(1, requestedUser.getUsername());

			rs = ps.executeQuery(); ps.close();

			
			if (!rs.next()){ rs.close(); //Namnet är inte i användning
				return true;
			}
			
	        return false;
	        
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}
	
	public static boolean signup(LoginCredentials requestedUser, PublicPlayer requestedPlayer) throws NamingException, SQLException{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();				
			
			String query = "CREATE (u:User {name:?, password:?, created: TIMESTAMP()})-[c:Created {created: TIMESTAMP()}]->(p:Player { name: ? }) "
					+ "CREATE (u)-[i:Is]->(p)"
					+ "RETURN id(p)";
			
			ps = con.prepareStatement(query);

			ps.setString(1, requestedUser.getUsername());
			ps.setString(2, requestedUser.getPassword());
			ps.setString(3, requestedPlayer.getPlayerName());
		
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
	
	public static User signin(LoginCredentials loginCredentials) throws NamingException, SQLException{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {	
			con = Database.getConnection();

			String query = "MATCH (u:User) WHERE u.name = ? AND u.password = ? RETURN id(u), PROPERTIES(u)";

			ps = con.prepareStatement(query);
      		ps.setString(1, loginCredentials.getUsername());
      		ps.setString(2, loginCredentials.getPassword());

      		rs = ps.executeQuery();
		
			if (rs.next()) { //Användaren finns
				return new User(rs.getInt("id(u)"), (Map)rs.getObject("PROPERTIES(u)"));
			}
			
			return null;
			
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
		
	}

	public static boolean requestResetPassword(LoginCredentials loginCredentials) {
		// TODO Auto-generated method stub
		return true;
	}

	public static boolean setNewPassword(LoginCredentials loginCredentials) {
		// TODO Auto-generated method stub
		return true;
	}
}
