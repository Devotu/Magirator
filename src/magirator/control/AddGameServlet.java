package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.Deck;
import magirator.beans.Play;
import magirator.beans.Player;
import magirator.beans.Result;

public class AddGameServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		
		try {
			
			getServletContext().log("-- AddGame --");
			getServletContext().log("-  AddGame -> Collecting data");

			int playerDeckId = Integer.parseInt(request.getParameter("playedDeck"));
			int opponentDeckId = Integer.parseInt(request.getParameter("opponentDeck"));
			String comment = request.getParameter("comment");
			String result = request.getParameter("result");

			getServletContext().log("-  AddGame -> Refining data");
			
			DeckHandler deckHandler = new DeckHandler();
			Deck playerDeck = deckHandler.getDeckById(playerDeckId);
			Deck opponentDeck = deckHandler.getDeckById(opponentDeckId);
			
			Play playerPlay = null;
			Play opponentPlay = null;
			
			if	("Win".equals(result)){
				playerPlay = new Play(1, true, comment);
				opponentPlay = new Play(2, false, "");
			} else if ("Draw".equals(result)){
				playerPlay = new Play(0, true, comment);
				opponentPlay = new Play(0, false, "");
			} else {
				playerPlay = new Play(2, true, comment);
				opponentPlay = new Play(1, false, "");
			}
			
			PlayerHandler playerHandler = new PlayerHandler();
			Player playerPlayer = (Player) playerHandler.getPlayerByDeck(playerDeckId);
			Player opponentPlayer = (Player) playerHandler.getPlayerByDeck(opponentDeckId);
			
			Result playerResult = new Result(playerDeck, playerPlay, playerPlayer);
			Result opponentResult = new Result(opponentDeck, opponentPlay, opponentPlayer);
			
			List<Result> results = new ArrayList<>();
			results.add(playerResult);
			results.add(opponentResult);
			
			GameHandler gameHandler = new GameHandler();
			
			getServletContext().log("-  AddGame -> Adding game");
			gameHandler.addGame(results);			
				
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

