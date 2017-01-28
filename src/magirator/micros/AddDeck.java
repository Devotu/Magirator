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
import magirator.support.Error;

/**
 * Servlet implementation class AddDeck
 */
@WebServlet(description = "Add a new deck to the current user", urlPatterns = { "/AddDeck" })
public class AddDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		getServletContext().log("-- AddDeck --");
		
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		    	sb.append(line);
		} 
		catch (Exception e) 
		{ 
			res.getWriter().write( Error.printStackTrace(e) );
		}

		JsonObject data = null;
		
		JsonParser parser = new JsonParser();

		data = (JsonObject) parser.parse(sb.toString());
		
		Deck deck = new Deck(data);
		
		DatabaseParams dp = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (u:User) WHERE id(u)=?";
			query += "CREATE (d:Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true})";
			query += "CREATE (u)-[r:Use]->(d)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, 0);
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
			
		} catch (Exception e){
			res.getWriter().write( Error.printStackTrace(e) );
		} finally {
			try {
				if (rs != null) rs.close();
				if (st != null) st.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				res.getWriter().write( Error.printStackTrace(e) );
			}
		}

		res.setContentType("text/plain");
		res.setCharacterEncoding("UTF-8");
		res.getWriter().write("Sucess!");

		getServletContext().log("-- AddDeck -- Done");
	}

}
