package magirator.control;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;

public class DeleteDeckServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- DeleteDeck --");
		getServletContext().log("-  DeleteDeck -> Collecting data");
		
		int deckId = Integer.parseInt(request.getParameter("deckid"));
		
		DeckHandler deckHandler = new DeckHandler();
				
		try {
			getServletContext().log("-  DeleteDeck -> Deleting deck");
			
			boolean success = deckHandler.deleteDeck(deckId);
			
			getServletContext().log("-  DeleteDeck -> " + (success? "Deleted deck " + deckId : "Failed to delete deck " + deckId));
			
				
		} catch (Exception ex) {
			getServletContext().log("-  DeleteDeck -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}

		getServletContext().log("-- DeleteDeck -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

