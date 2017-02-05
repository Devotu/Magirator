package magirator.control;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import magirator.dataobjects.Alteration;
import magirator.model.neo4j.*;

public class GetAlterationServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetAlteration --");
				
		getServletContext().log("-  GetAlteration -> Collecting data for deck " + request.getParameter("id"));
		
		int alterationId = Integer.parseInt(request.getParameter("id"));
		
		AlterationHandler alterationHandler = new AlterationHandler();
		Alteration alteration = null;
				
		try {
			getServletContext().log("-  GetAlteration -> Getting alteration " + alterationId);
			alteration = alterationHandler.getAlterationById(alterationId);
				
		} catch (Exception ex) {
			getServletContext().log("-  GetAlteration -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");
		}		
		
		if (alteration != null) {
			getServletContext().log("-  GetAlteration -> Found an alteration");
		} else {
			getServletContext().log("-  GetAlteration -> No alteration found");
		}
		
		request.setAttribute("alteration", alteration);

		getServletContext().log("-- GetAlteration -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

