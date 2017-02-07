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
import magirator.support.Database;

public class Games {

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