package magirator.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

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
		
        JsonArray returnJson = new JsonArray();
        //Most popular
        returnJson.add(new JsonPrimitive("Standard"));
        returnJson.add(new JsonPrimitive("Booster draft"));
        returnJson.add(new JsonPrimitive("Sealed deck"));
        returnJson.add(new JsonPrimitive("Block"));
        
        //Constructed
        //returnJson.add(new JsonPrimitive("Standard"));
        returnJson.add(new JsonPrimitive("Modern"));
        returnJson.add(new JsonPrimitive("Commander"));
        returnJson.add(new JsonPrimitive("Legacy"));
        returnJson.add(new JsonPrimitive("Vintage"));
        returnJson.add(new JsonPrimitive("Team unified constructed"));
        //returnJson.add(new JsonPrimitive("Block"));
        
        
        //Limited
        //returnJson.add(new JsonPrimitive("Booster draft"));
        //returnJson.add(new JsonPrimitive("Sealed deck"));
        returnJson.add(new JsonPrimitive("Conspiracy"));
        returnJson.add(new JsonPrimitive("Team Booster draft"));
        returnJson.add(new JsonPrimitive("Team Sealed deck"));
        
        //Online
        returnJson.add(new JsonPrimitive("Freeform"));
        returnJson.add(new JsonPrimitive("Momir"));
        returnJson.add(new JsonPrimitive("Pauper"));
        returnJson.add(new JsonPrimitive("Planeswalker"));
        returnJson.add(new JsonPrimitive("Duels"));
        
        //Custom
        returnJson.add(new JsonPrimitive("Custom"));
        returnJson.add(new JsonPrimitive("Booster handover"));
        
        res.setContentType("application/json");
        res.getWriter().write(returnJson.toString());

		getServletContext().log("-- GetFormats -- Done");
	}

}
