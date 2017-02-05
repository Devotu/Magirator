package magirator.control;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import magirator.dataobjects.ListContainer;
import magirator.dataobjects.Player;
import magirator.model.neo4j.GameHandler;

public class GetUserGamesServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetGames (User) --");
		
		ListContainer gameList = new ListContainer();
		
		getServletContext().log("-  GetGames -> Collecting data");
		
		HttpSession session = request.getSession();
		Player player = (Player) session.getAttribute("player");
		
		/**
		 * -1 = only unconfirmed
		 * 0 = does not matter
		 * 1 = only confirmed
		 */
		int confimedStatus; 
		
		if (request.getParameter("confimedStatus") != null){
			confimedStatus = Integer.parseInt(request.getParameter("confimedStatus"));
		} else {
			confimedStatus = 0;
		}
		
		GameHandler gameHandler = new GameHandler();		
		
		if (confimedStatus == -1){ //Get all unconfirmed
			try {
				getServletContext().log("-  GetGames -> Getting games belonging to user " + player.getId());
				gameList.setListItems(gameHandler.listPlayerGamesNotConfirmed(player.getId()));
				gameList.setSortOptions(gameHandler.getSortables());
				gameList.setFilterOptions(gameHandler.getFilterables());
					
			} catch (Exception ex) {
				getServletContext().log("-  GetGames -- Error -- " + ex.getMessage());
				getServletContext().log(getStackTrace(ex));
				throw new ServletException("Something databazy went wrong");

			}
		} else {
			getServletContext().log("-  GetGames -> No result: confimedStatus is " + confimedStatus);
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

