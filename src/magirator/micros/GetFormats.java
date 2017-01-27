package magirator.micros;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import magirator.model.neo4j.DatabaseParams;
import magirator.objects.Deck;
import magirator.support.DatabaseHelper;
import magirator.support.ErrorHandler;
import magirator.support.ParameterHelper;

/**
 * Servlet implementation class GetFormats
 */
@WebServlet("/GetFormats")
public class GetFormats extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		getServletContext().log("-- GetFormats --");
		
		getServletContext().log("-  GetFormats -> Returning formats");
		
        JsonArray returnJson = new JsonArray();
        returnJson.add(new JsonPrimitive("Standard"));
        returnJson.add(new JsonPrimitive("Block"));
        returnJson.add(new JsonPrimitive("Limited"));
        returnJson.add(new JsonPrimitive("Modern"));
        returnJson.add(new JsonPrimitive("Vintage"));
        res.setContentType("application/json");
        res.getWriter().write(returnJson.toString());

		getServletContext().log("-- GetFormats -- Done");
	}

}
