package magirator.model.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;

import magirator.dataobjects.Deck;
import magirator.dataobjects.Player;
import magirator.support.Database;

public class Decks {

	public static boolean addDeck(Player player, Deck deck) throws SQLException, NamingException {

		DatabaseParams dp = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = Database.getConnection();

			String query = "MATCH (p:Player) WHERE id(p)=?";
			query += "CREATE (d:Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true})";
			query += "CREATE (p)-[r:Use]->(d)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, player.getId());
			ps.setString(2, deck.getName());
			ps.setString(3, deck.getFormat());
			ps.setBoolean(4, deck.getBlack());
			ps.setBoolean(5, deck.getWhite());
			ps.setBoolean(6, deck.getRed());
			ps.setBoolean(7, deck.getGreen());
			ps.setBoolean(8, deck.getBlue());
			ps.setBoolean(9, deck.getColorless());
			ps.setString(10, deck.getTheme());

			ps.executeUpdate();

			return true;

		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (con != null)
				con.close();
		}
	}

}
