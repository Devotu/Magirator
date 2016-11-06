package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.Player;

public class AddDeckServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- AddDeck --");
		getServletContext().log("-  AddDeck -> Collecting data");

		String name = request.getParameter("name");
		String format = request.getParameter("format");
		String colors[] = request.getParameterValues("colors");
		String theme = request.getParameter("theme");

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

		getServletContext().log("-- AddDeck -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

