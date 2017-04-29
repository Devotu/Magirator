package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import magirator.data.objects.Rating;
import magirator.support.Database;

public class Ratings {
	
	public static boolean addRatingToResult(int playerId, Rating r, int gameId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = Database.getConnection();
			
			String query = ""
					+ "MATCH (p:Player)-[:Use]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE id(p)=? AND id(g)=? "
					+ "CREATE (p)-[:Gave]->(rt:Rating {speed: ?, strength: ?, synergy: ?, control: ?})-[:To]->(r)";

			ps = con.prepareStatement(query);
			
			ps.setInt(1, playerId);
			ps.setInt(2, gameId);
			ps.setInt(3, r.getSpeed());
			ps.setInt(4, r.getStrength());
			ps.setInt(5, r.getSynergy());
			ps.setInt(6, r.getControl());
			
			ps.executeQuery();
			
			return true;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

}
