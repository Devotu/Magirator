package magirator.micros;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import magirator.model.neo4j.DatabaseParams;
import magirator.objects.Deck;
import magirator.objects.Player;
import magirator.support.Error;
import magirator.support.Json;

/**
 * Servlet implementation class AddDeck
 */
@WebServlet(description = "Add a new deck to the current user", urlPatterns = { "/AddDeck" })
public class AddDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getServletContext().log("-- AddDeck --");
		
		JsonObject result = new JsonObject();
		result.addProperty("result", "Something went wrong adding your deck");
		
		JsonObject data = Json.parseRequestData(request);
		
		Player player = new Player( data.get("player").getAsJsonObject() );
		Deck deck = new Deck( data.get("deck").getAsJsonObject() );
		
		DatabaseParams dp = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

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
			
			result.addProperty("result", "Success");
			
		} catch (Exception e){
			response.getWriter().write( Error.printStackTrace(e) );
		} finally {
			try {
				if (rs != null) rs.close();
				if (st != null) st.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				response.getWriter().write( Error.printStackTrace(e) );
			}
		}

        response.setContentType("application/json");
        response.getWriter().write(result.toString());

		getServletContext().log("-- AddDeck -- Done");
	}

}
