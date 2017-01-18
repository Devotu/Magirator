package magirator.micros;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import magirator.model.neo4j.DeckHandler;
import magirator.objects.Player;
import magirator.support.ParameterHelper;

/**
 * Servlet implementation class AddDeck
 */
@WebServlet(description = "Add a new deck to the current user", urlPatterns = { "/AddDeck" })
public class AddDeck extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddDeck() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		getServletContext().log("-- AddDeck --");
		getServletContext().log("-  AddDeck -> Collecting data");

		String json = ParameterHelper.returnParameter(req, "deck");
		
		getServletContext().log("-- AddDeck -- Done");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		getServletContext().log("-- AddDeck --");
		getServletContext().log("-  AddDeck -> Collecting data");
		
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		    	sb.append(line);
		} 
		catch (Exception e) 
		{ /*report an error*/ }

		JSONObject jsonObject = null;
		
		try {
			jsonObject =  HTTP.toJSONObject(sb.toString());			
		} 
		catch (JSONException e) {
		    // crash and burn
			throw new IOException("Error parsing JSON request string");
		}

		String json = ParameterHelper.returnParameter(req, "deck");

		/*
		for(String c : colors){
			getServletContext().log("-  AddDeck -> has color " + c);
		}
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");;
		
		DeckHandler deckHandler = new DeckHandler();
				
		try {
			getServletContext().log("-  AddDeck -> Adding deck");
			deckHandler.addDeckToUser(player.getId(), name, format, colors, theme);
			
				
		} catch (Exception ex) {
			getServletContext().log("-  AddDeck -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}
		
		
		boolean[] colors = this.parseColors(scolors);
		
		try {
			Context initContext = new InitialContext();
			Context webContext = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) webContext.lookup("jdbc/MagiratorDB");
			con = ds.getConnection();

			String query = "MATCH (u:User) WHERE id(u)=?";
			query += "CREATE (d:Deck { name: ?, format: ?, black: ?, white: ?, red: ?, green: ?, blue: ? ,colorless: ?, theme: ?, created: TIMESTAMP(), active:true})";
			query += "CREATE (u)-[r:Use]->(d)";

			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, userid);
			ps.setString(2, name);
			ps.setString(3, format);
			ps.setBoolean(4, colors[0]);//Black
			ps.setBoolean(5, colors[1]);//White
			ps.setBoolean(6, colors[2]);//Red
			ps.setBoolean(7, colors[3]);//Green
			ps.setBoolean(8, colors[4]);//Blue
			ps.setBoolean(9, colors[5]);//Colorless
			ps.setString(10, theme);
			
			ps.executeUpdate();
			
		} catch (Exception ex){
			throw ex;
		} finally {
			if (rs != null) rs.close();
			if (st != null) st.close();
			if (con != null) con.close();
		}
		*/

		getServletContext().log("-- AddDeck -- Done");
	}

}
