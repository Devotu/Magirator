package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.UserInfo;
import magirator.beans.Deck;
import magirator.beans.Game;
import magirator.beans.GameResult;

public class GetGameServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetGame --");
				
		getServletContext().log("-  GetGame -> Collecting data for game " + request.getParameter("id"));
		
		int gameid = Integer.parseInt(request.getParameter("id"));
		
		HttpSession session = request.getSession();
		Deck deck = (Deck)session.getAttribute("deck");
		int deckid = deck.getDeckid();
		
		GameHandler gameHandler = new GameHandler();
		Game game = new Game();
				
		try {
			getServletContext().log("-  GetGame -> Getting game " + gameid);
			game = gameHandler.getResultsInGame(gameid);
				
		} catch (Exception ex) {
			getServletContext().log("-  GetGame -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");
		}		
		
		if (deck != null) {
			getServletContext().log("-  GetGame -> Found a deck");
		} else {
			getServletContext().log("-  GetGame -> No deck found");
		}
		
		request.setAttribute("game", game);

		getServletContext().log("-- GetGame -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

