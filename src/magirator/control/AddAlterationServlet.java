package magirator.control;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;

public class AddAlterationServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		
		try {
			
			getServletContext().log("-- AddAlteration --");
			getServletContext().log("-  AddAlteration -> Collecting data");

			int deckId = Integer.parseInt(request.getParameter("deckId"));
			String comment = request.getParameter("comment");
			
			String name = request.getParameter("name");
			String format = request.getParameter("format");
			String colors[] = request.getParameterValues("colors");
			String theme = request.getParameter("theme");
						
			getServletContext().log("-  AddAlteration -> Adding game");
			
			AlterationHandler alterationHandler = new AlterationHandler();
			int newDeckId = alterationHandler.AlterDeck(deckId, name, format, colors, theme, comment);
			
			request.setAttribute("newDeckId", newDeckId);
				
		} catch (Exception ex) {
			getServletContext().log("-  AddAlteration -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}

		getServletContext().log("-- AddAlteration -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

