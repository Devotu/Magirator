package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import magirator.data.entities.Tag;
import magirator.support.Database;

public class Tags {
	
	public static boolean addTagsToResultsInGame(ArrayList<Tag> tags, int gameId) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			con = Database.getConnection();
			
			String query = ""
					+ "MATCH (p1:Player), (p2:Player)-[:Use]->(:Deck)-[:Got]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE id(p1)=? AND id(p2)=? AND id(g)=? "
					+ "MERGE (p1)-[:Put]->(t:Tag {polarity: ?, tag: ?}) "
					+ "CREATE (t)-[:On]->(r)";

			ps = con.prepareStatement(query);
			
			for (Tag t : tags){				
				ps.setInt(1, t.getTagger());
				ps.setInt(2, t.getTagged());
				ps.setInt(3, gameId);
				ps.setInt(4, t.getPolarity());
				ps.setString(5, t.getTag());
				
				ps.executeQuery();
			}
			
			return true;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (ps != null) ps.close();
			if (con != null) con.close();
		}
	}

	public static List<Tag> getTagsInGame(int gameId) throws Exception {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			

			String query = ""
					+ "MATCH (p:Player)-[:Use|:Used]->(:Deck)-[:Got]->(r:Result)-[]->(g:Game), (tp:Player)-[:Put]->(t:Tag)-[:On]->(r) "
					+ "WHERE id(g)=? "
					+ "RETURN id(tp), PROPERTIES(t), id(r)";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, gameId);
      		
      		rs = ps.executeQuery();
      		
      		List<Tag> tags = new ArrayList<Tag>();
			
			while (rs.next()) {
				Tag t = new Tag(
							rs.getInt("id(tp)"),
							rs.getInt("id(r)"),
							(Map)rs.getObject("PROPERTIES(t)")
						);
				
				tags.add(t);
			}
			
			return tags;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
		}		
	}

}
