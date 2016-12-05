package magirator.control;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import magirator.beans.ListContainer;
import magirator.beans.Player;
import magirator.model.neo4j.DeckHandler;

public class GetDecksServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetDecks --");
		
		ListContainer deckList = new ListContainer();
		//List deckList = new ArrayList();
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		getServletContext().log("-  GetDecks -> Collecting data");
		
		DeckHandler deckHandler = new DeckHandler();
				
		try {
			getServletContext().log("-  GetDecks -> Getting decks belonging to user " + player.getId());
			deckList.setListItems(deckHandler.listDecksBelongingToUser(player.getId()));
			deckList.setSortOptions(deckHandler.getSortables());
			deckList.setFilterOptions(deckHandler.getFilterables());
				
		} catch (Exception ex) {
			getServletContext().log("-  GetDecks -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}
		
		getServletContext().log("-  GetDecks -> Found " + deckList.getListItems().size() + " decks");
		getServletContext().log("-  GetDecks -> Nubmer of sort options " + deckList.getSortOptions().length);
		getServletContext().log("-  GetDecks -> Number of filter options " + deckList.getFilterOptions().size());
		
		request.setAttribute("deckListContainer", deckList);

		getServletContext().log("-- GetDecks -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

