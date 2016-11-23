package magirator.control;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;

public class ToggleDeckServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- ToggleDeck --");
		getServletContext().log("-  ToggleDeck -> Collecting data");
		
		int deckId = Integer.parseInt(request.getParameter("deckid"));
		
		DeckHandler deckHandler = new DeckHandler();
				
		try {
			getServletContext().log("-  ToggleDeck -> Toggling deck");
			
			boolean success = deckHandler.toggleDeck(deckId);
			
			getServletContext().log("-  ToggleDeck -> " + (success? "Toggled deck " + deckId : "Failed to toggle deck " + deckId));
			
				
		} catch (Exception ex) {
			getServletContext().log("-  ToggleDeck -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}

		getServletContext().log("-- ToggleDeck -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

