package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import magirator.dataobjects.Deck;
import magirator.dataobjects.Play;
import magirator.dataobjects.Player;
import magirator.dataobjects.Result;
import magirator.model.neo4j.*;

public class AddGameServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		
		try {
			
			getServletContext().log("-- AddGame --");
			getServletContext().log("-  AddGame -> Collecting data");

			int playerDeckId = Integer.parseInt(request.getParameter("playedDeck"));
			String deckPlaces =  request.getParameter("deckList");
			String playerComment = request.getParameter("comment");
			boolean draw = Boolean.valueOf(request.getParameter("draw"));

			getServletContext().log("-  AddGame -> Refining data");
			
			DeckHandler deckHandler = new DeckHandler();
			PlayerHandler playerHandler = new PlayerHandler();
			
			List<Result> results = new ArrayList<>();
			
			String[] deckPlaceArray = deckPlaces.split(",");
			
			int place = 0;
			for (String s : deckPlaceArray){
				
				if (!draw){
					place++;
				}
				
				int deckId = Integer.parseInt(s);
				
				Deck deck = deckHandler.getDeckById(deckId);
				
				String comment = "Not commented";
				
				Play play = new Play(0, place, false, comment);
				
				if (deckId == playerDeckId){
					play.setComment(playerComment);
					play.setConfirmed(1);
				}
				
				Result r = new Result(deck, play, null);
				results.add(r);
			}
			
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

