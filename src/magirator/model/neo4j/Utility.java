package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import magirator.support.Database;

public class Utility {
	
	public static int getUniqueId() throws Exception{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = ""
					+ "MERGE (id:GlobalUniqueId) "
					+ "ON CREATE SET id.count = 1 "
					+ "ON MATCH SET id.count = id.count + 1 "
					+ "RETURN id.count AS generated_id ";
			
			ps = con.prepareStatement(query);			
			rs = ps.executeQuery();
			
			if	(rs.next()){
				
				return rs.getInt("generated_id");
			}
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
		
		return 0;
	}
}
