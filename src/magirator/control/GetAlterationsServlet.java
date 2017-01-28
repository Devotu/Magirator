package magirator.control;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import magirator.model.neo4j.AlterationHandler;
import magirator.objects.ListContainer;

public class GetAlterationsServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetAlterations --");
		
		ListContainer alterationList = new ListContainer();
		
		getServletContext().log("-  GetAlterations -> Collecting data");
		
		int deckid = Integer.parseInt(request.getParameter("id"));
		
		//If deck has been altered within the same request
		if	(request.getAttribute("newDeckId") != null){
			deckid = (int) request.getAttribute("newDeckId");
		}
		
		AlterationHandler alterationHandler = new AlterationHandler();	
				
		try {
			getServletContext().log("-  GetAlterations -> Getting alterations belonging to deck " + deckid);
			alterationList.setListItems(alterationHandler.listAlterationsBelongingToDeck(deckid));
			alterationList.setSortOptions(alterationHandler.getSortables());
			alterationList.setFilterOptions(alterationHandler.getFilterables());
				
		} catch (Exception ex) {
			getServletContext().log("-  GetAlterations -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}
		
		getServletContext().log("-  GetAlterations -> Found " + alterationList.getListItems().size() + " decks");
		getServletContext().log("-  GetAlterations -> Nubmer of sort options " + alterationList.getSortOptions().length);
		getServletContext().log("-  GetAlterations -> Number of filter options " + alterationList.getFilterOptions().size());
		
		request.setAttribute("alterationListContainer", alterationList);

		getServletContext().log("-- GetAlterations -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

