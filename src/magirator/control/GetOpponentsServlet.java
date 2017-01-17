package magirator.control;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import magirator.model.neo4j.PlayerHandler;
import magirator.objects.Opponent;
import magirator.objects.Player;

public class GetOpponentsServlet extends HttpServlet {
	
	public synchronized void service(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- GetOpponents --");
		
		List<Opponent> opponents = new ArrayList<Opponent>();
		
		getServletContext().log("-  GetOpponents -> Collecting data");
		
		HttpSession session = request.getSession();
		Player player = (Player)session.getAttribute("player");
		
		PlayerHandler playerHandler = new PlayerHandler();		
				
		try {
			getServletContext().log("-  GetOpponents -> Getting opponents to user " + player.getName() + " (" + player.getId() + ")");
			opponents = playerHandler.getPlayerOpponents(player.getId());
				
		} catch (Exception ex) {
			getServletContext().log("-  GetOpponents -- Error -- " + ex.getMessage());
			getServletContext().log(getStackTrace(ex));
			throw new ServletException("Something databazy went wrong");

		}
		
		getServletContext().log("-  GetOpponents -> Found " + opponents.size() + " opponents");
		
		request.setAttribute("opponents", opponents);

		getServletContext().log("-- GetOpponents -- Done");
	}
	
	public static String getStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}
}

