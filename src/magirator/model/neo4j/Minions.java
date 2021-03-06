package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import magirator.data.entities.Minion;
import magirator.data.entities.Player;
import magirator.data.entities.User;
import magirator.data.interfaces.Player;
import magirator.support.Database;

public class Minions {

	public static Minion addMinion(User user, Player minion) throws Exception {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			String query = "" +
					"MATCH (u:User) "
					+ "WHERE id(u) = ? "
					+ "CREATE (u)-[c:Created {created: TIMESTAMP()}]->(m:Minion { name: ? }) "
					+ "RETURN id(m), PROPERTIES(m)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, user.getId());
			ps.setString(2, minion.getName());

			rs = ps.executeQuery();

			if (rs.next()) {
				return new Minion( rs.getInt("id(m)"), (Map)rs.getObject("PROPERTIES(m)") );
			}

			return null;

		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

	public static List<Minion> getUserMinions(User user) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();
	
			String query = ""
					+ "MATCH (u:User)-[:Created]->(m:Minion) "
					+ "WHERE id(u) = ? "
					+ "RETURN id(m), PROPERTIES(m)";
	
	  		ps = con.prepareStatement(query);
	  		ps.setInt(1, user.getId());
	
	  		rs = ps.executeQuery();
	  		
	  		List<Minion> minions = new ArrayList<>();
			
			while (rs.next()) {
				minions.add( new Minion( rs.getInt("id(m)"), (Map)rs.getObject("PROPERTIES(m)") ) );
			}
	
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return minions;
			
		} catch (Exception ex){
			throw ex;
		}		
	}
	
	public static Minion getMinion(int minionId) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();
	
			String query = ""
					+ "MATCH (m:Minion) "
					+ "WHERE id(m) = ? "
					+ "RETURN id(m), PROPERTIES(m)";
	
	  		ps = con.prepareStatement(query);
	  		ps.setInt(1, minionId);
	
	  		rs = ps.executeQuery();
			
			if (rs.next()) {
				return new Minion( rs.getInt("id(m)"), (Map)rs.getObject("PROPERTIES(m)") );
			}
	
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return null;
			
		} catch (Exception ex){
			throw ex;
		}		
	}
	
	public static boolean isMinionOfPlayer(int minionId, int playerId) throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();
	
			String query = ""
					+ "MATCH (p:Player)<-[:Is]-(u:User)-[:Created]->(m:Minion) "
					+ "WHERE id(p)=? AND id(m)=? "
					+ "RETURN id(m)";
	
	  		ps = con.prepareStatement(query);
	  		ps.setInt(1, minionId);
	  		ps.setInt(2, playerId);
	
	  		rs = ps.executeQuery();
			
			if (rs.next()) {
				return true;
			}
	
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		}		
	}

}


