package magirator.control;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;

public class ConfirmGameServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2052044123793591591L;

	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		
		try {
			
			getServletContext().log("-- ConfirmGame --");
			getServletContext().log("-  ConfirmGame -> Collecting data");

			int playId = Integer.parseInt(request.getParameter("playId"));
			String decision = request.getParameter("action");			
			String comment = request.getParameter("comment");

			getServletContext().log("-  ConfirmGame -> Refining data");
			
			GameHandler gameHandler = new GameHandler();
			
			if ("go".equals(decision)){

				getServletContext().log("-  ConfirmGame -> Confirming game");
				gameHandler.confirmGame(playId, true, comment);
				
			} else {

				getServletContext().log("-  ConfirmGame -> Rejecting game");
				gameHandler.confirmGame(playId, false, comment);
			}
				
		} catch (Exception ex) {
			getServletContext().log("-  ConfirmGame -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}

		getServletContext().log("-- ConfirmGame -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

