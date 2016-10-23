package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.GameResult;
import magirator.beans.UserInfo;

public class AddGameServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- AddGame --");
		getServletContext().log("-  AddGame -> Collecting data");

		int playedDeck = Integer.parseInt(request.getParameter("playedDeck"));
		int opponentDeck = Integer.parseInt(request.getParameter("opponentDeck"));
		String comment = request.getParameter("comment");
		String result = request.getParameter("result");

		getServletContext().log("-  AddGame -> Refining data");
		
		List<GameResult> results = new ArrayList<>();
		GameResult playerResult = new GameResult();
		playerResult.setDeckId(playedDeck);
		playerResult.setComment(comment);
		GameResult opponentResult = new GameResult();
		opponentResult.setDeckId(opponentDeck);
		
		if	("Win".equals(result)){
			playerResult.setPlace(1);
			opponentResult.setPlace(2);
		} else if ("Draw".equals(result)){
			playerResult.setPlace(0);
			opponentResult.setPlace(0);
		} else {
			playerResult.setPlace(2);
			opponentResult.setPlace(1);
		}
		
		results.add(playerResult);
		results.add(opponentResult);
		
		GameHandler gameHandler = new GameHandler();
				
		try {
			getServletContext().log("-  AddGame -> Adding game");
			gameHandler.addTwoPlayerGame(results);			
				
		} catch (Exception ex) {
			getServletContext().log("-  AddGame -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}

		getServletContext().log("-- AddGame -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

