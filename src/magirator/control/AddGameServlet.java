package magirator.control;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.beans.UserInfo;

public class AddGameServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- AddGame --");
		getServletContext().log("-  AddGame -> Collecting data");

		String name = request.getParameter("name");
		String format = request.getParameter("format");
		String colors[] = request.getParameterValues("colors");
		String theme = request.getParameter("theme");

		for(String c : colors){
			getServletContext().log("-  AddGame -> has color " + c);
		}
		
		HttpSession session = request.getSession();
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");;
		
		DeckHandler deckHandler = new DeckHandler();
				
		try {
			getServletContext().log("-  AddGame -> Adding deck");
			deckHandler.addDeckToUser(userInfo.getId(), name, format, colors, theme);
			
				
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

