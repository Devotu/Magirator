package magirator.control;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.GameResult;

public class GetGameServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetGame --");
				
		getServletContext().log("-  GetGame -> Collecting data for game " + request.getParameter("id"));
		
		int gameid = Integer.parseInt(request.getParameter("id"));
		
		GameHandler gameHandler = new GameHandler();
		GameResult gameResult = null;
				
		try {
			getServletContext().log("-  GetGame -> Getting game " + gameid);
			gameResult = gameHandler.getResultsInGame(gameid);
				
		} catch (Exception ex) {
			getServletContext().log("-  GetGame -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");
		}		
		
		if (gameResult != null) {
			getServletContext().log("-  GetGame -> Found a game");
		} else {
			getServletContext().log("-  GetGame -> No game found");
		}
		
		request.setAttribute("game", gameResult);

		getServletContext().log("-- GetGame -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

