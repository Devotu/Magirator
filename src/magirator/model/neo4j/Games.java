package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.NamingException;
import magirator.dataobjects.Deck;
import magirator.dataobjects.Play;
import magirator.dataobjects.Result;
import magirator.support.Database;

public class Games {
	
	public static boolean addGame(ArrayList<Result> results) throws Exception {
		
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();			
			
			String query = "CREATE (g:Game {created: TIMESTAMP()}) RETURN id(g)";
			PreparedStatement ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			if	(rs.next()){
				int gameId = rs.getInt("id(g)");
				
				query = "MATCH (g:Game), (d:Deck)";
				query += "WHERE id(g) = ? AND id(d) = ?";
				query += "CREATE (d)-[r:Played {place: ?, comment: ?, confirmed:? }]->(g)";					
				ps = con.prepareStatement(query);
				
				for (Result r : results){
					ps.setInt(1, gameId);
					ps.setInt(2, r.getDeck().getDeckid());
					ps.setInt(3, r.getPlay().getPlace());
					ps.setString(4, r.getPlay().getComment());
					ps.setInt(5, r.getPlay().getConfirmed() ? 1 : 0);
					
					ps.executeUpdate();						
				}
				
				return true;			
			}
			
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (con != null) con.close();
			
		} catch (Exception ex){
			throw ex;
		}
		
		return false;
	}

	public static ArrayList<Play> getDeckPlayed(Deck deck) throws NamingException, SQLException {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = Database.getConnection();

			String query = "MATCH (d:Deck)-[p:Played]->(:Game) WHERE id(d)=? RETURN id(p), PROPERTIES(p)";

      		PreparedStatement ps = con.prepareStatement(query);
      		ps.setInt(1, deck.getDeckid());

      		rs = ps.executeQuery();
      		
      		ArrayList<Play> plays = new ArrayList<Play>();
			
			while (rs.next()) {
				plays.add( new Play( rs.getInt("id(p)"), (Map)rs.getObject("PROPERTIES(p)") ) );
			}

			return plays;
			
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}

	}

}