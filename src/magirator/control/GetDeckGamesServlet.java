package magirator.control;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import magirator.beans.ListContainer;
import magirator.model.neo4j.GameHandler;

public class GetDeckGamesServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetGames (Deck) --");
		
		ListContainer gameList = new ListContainer();
		
		getServletContext().log("-  GetGames -> Collecting data");
		
		int deckid = Integer.parseInt(request.getParameter("id"));
		
		//If deck has been altered within the same request
		if	(request.getAttribute("newDeckId") != null){
			deckid = (int) request.getAttribute("newDeckId");
		}
		
		GameHandler gameHandler = new GameHandler();		
				
		try {
			getServletContext().log("-  GetGames -> Getting games belonging to deck " + deckid);
			gameList.setListItems(gameHandler.listGamesBelongingToDeck(deckid));
			gameList.setSortOptions(gameHandler.getSortables());
			gameList.setFilterOptions(gameHandler.getFilterables());
				
		} catch (Exception ex) {
			getServletContext().log("-  GetGames -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}
		
		getServletContext().log("-  GetGames -> Found " + gameList.getListItems().size() + " decks");
		getServletContext().log("-  GetGames -> Nubmer of sort options " + gameList.getSortOptions().length);
		getServletContext().log("-  GetGames -> Number of filter options " + gameList.getFilterOptions().size());
		
		request.setAttribute("gameListContainer", gameList);

		getServletContext().log("-- GetGames -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

