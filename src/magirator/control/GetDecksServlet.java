package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.UserInfo;
import magirator.beans.ListDeck;

public class GetDecksServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetDecks --");
		
		List deckList = new ArrayList();
		
		HttpSession session = request.getSession();
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
		
		getServletContext().log("-  GetDecks -> Collecting data");
		
		DeckHandler deckHandler = new DeckHandler();
				
		try {
			getServletContext().log("-  GetDecks -> Getting decks belonging to user " + userInfo.getId());
			deckList = deckHandler.getDecksBelongingToUser(userInfo.getId());
				
		} catch (Exception ex) {
			getServletContext().log("-  GetDecks -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}
		
		getServletContext().log("-  GetDecks -> Found " + deckList.size() + " decks");
		
		session.setAttribute("deckList", deckList.toArray());

		getServletContext().log("-- GetDecks -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

