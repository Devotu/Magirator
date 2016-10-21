package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.UserInfo;
import magirator.beans.Deck;

public class GetDeckServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetDeck --");
				
		getServletContext().log("-  GetDeck -> Collecting data for deck " + request.getParameter("id"));
		
		int deckid = Integer.parseInt(request.getParameter("id"));
		
		DeckHandler deckHandler = new DeckHandler();
		Deck deck = null;
				
		try {
			getServletContext().log("-  GetDeck -> Getting deck " + deckid);
			deck = deckHandler.getDeckById(deckid);
				
		} catch (Exception ex) {
			getServletContext().log("-  GetDeck -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");
		}
		
		
		if (deck != null) {
			getServletContext().log("-  GetDeck -> Found a deck");
		} else {
			getServletContext().log("-  GetDeck -> No deck found");
		}
		
		request.setAttribute("deck", deck);

		getServletContext().log("-- GetDeck -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

