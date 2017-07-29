package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import magirator.data.entities.Tag;
import magirator.support.Database;

public class Tags {
	
	/**
	 * @deprecated
	 */
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
				ps.setString(5, t.getText());
				
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
					+ "MATCH (tagger)-[:Put]->(t:Tag)-[:On]->(r:Result)-[:In]->(g:Game) "
					+ "WHERE g.id=? "
					+ "RETURN tagger.id, PROPERTIES(t), r.id";
			
			ps = con.prepareStatement(query);
      		
			ps.setInt(1, gameId);
      		
      		rs = ps.executeQuery();
      		
      		List<Tag> tags = new ArrayList<Tag>();
			
			while (rs.next()) {
				Tag t = new Tag(
							rs.getInt("tagger.id"),
							rs.getInt("r.id"),
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
	
	

	public static boolean addTagToEntity(Tag tag) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String query = ""
					+ ""
					+ "MATCH (tagger), (entity) "
					+ "WHERE tagger.id=? AND entity.id=? "
					+ "MERGE (tagger)-[:Put]->(t"+ Tag.neoMerge() +") "
					+ "SET t.id=?  "
					+ "CREATE (t)-[:On]->(entity)"
					+ "RETURN t";
			
			List<Object> params = new ArrayList<>();
			params.add(tag.getTagger());
			params.add(tag.getTagged());
			params.add(tag.getPolarity());
			params.add(tag.getText());
			params.add(Utility.getUniqueId());
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				return true;
			}
			
			return false;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	/**
	 * @param entity_id
	 * @return Json list of tags
	 * @throws Exception
	 */
	public static String getEntityTagsAsJson(int entity_id) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String query = ""
					+ "MATCH (t:Tag)-[:On]->(entity) "
					+ "WHERE entity.id=? "
					+ "RETURN collect({"
					+ "			id: t.id, "
					+ "			text: t.text, "
					+ "			polarity: t.polarity"
					+ "	}) AS tags";
			
			List<Object> params = new ArrayList<>();
			params.add(entity_id);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if(rs.next()){				
				List tMap = (List) rs.getObject("tags");
				String tJson = new Gson().toJson(tMap, List.class);
				return tJson;
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	

	/**
	 * @param tag_id
	 * @param entity_id
	 * @return number of entities still tagged with this tag
	 * @throws Exception
	 */
	public static int removeTagFromEntity(int tag_id, int entity_id) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String query = ""
					+ "MATCH (t:Tag)-[on:On]->(e) "
					+ "WHERE t.id=? AND e.id=? "
					+ "DETACH DELETE on "
					+ "WITH t "
					+ "MATCH (t:Tag)-[all_on:On]->() "
					+ "RETURN count(all_on) AS count_current";
			
			List<Object> params = new ArrayList<>();
			params.add(tag_id);
			params.add(entity_id);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				return rs.getInt("count_current");
			}
			
			return 0;
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	
	/**
	 * Warning, this will remove the tag completely and will thus dissapear from all entities tagged as such
	 * @param tag_id
	 * @throws Exception
	 */
	public static void deleteTag(int tag_id) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String query = ""
					+ "MATCH (t:Tag) "
					+ "WHERE t.id=? "
					+ "DETACH DELETE t";
			
			List<Object> params = new ArrayList<>();
			params.add(tag_id);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}
	
	
	/**
	 * @param tagger_id
	 * @return Json list of tags
	 * @throws Exception
	 */
	public static String getTaggerTagsAsJson(int tagger_id) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String query = ""
					+ "MATCH (tagger)-[:Put]->(t:Tag) "
					+ "WHERE tagger.id=? "
					+ "RETURN collect({"
					+ "			id: t.id, "
					+ "			text: t.text, "
					+ "			polarity: t.polarity"
					+ "	}) AS tags";
			
			List<Object> params = new ArrayList<>();
			params.add(tagger_id);
						
			con = Database.getConnection();			
			ps = con.prepareStatement(query);			
			ps = Database.setStatementParams(ps, params);
			
			rs = ps.executeQuery();
			
			if(rs.next()){				
				List tMap = (List) rs.getObject("tags");
				String tJson = new Gson().toJson(tMap, List.class);
				return tJson;
			}
			
			return "";
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (con != null) con.close();
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		}
	}

}
