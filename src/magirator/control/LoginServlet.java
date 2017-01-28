package magirator.control;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.*;
import javax.servlet.http.*;
import magirator.model.neo4j.*;
import magirator.objects.*;

public class LoginServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		getServletContext().log("-- Login --");
		getServletContext().log("-  Login -> Collecting data");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String viewName = request.getParameter("goView");
		String errorViewName = request.getParameter("errorView");
		int userid = -1;
		
		LoginHandler loginHandler = new LoginHandler();
		getServletContext().log("-  Login -> Logging in");		
		
		try {
			userid = loginHandler.login(username, password);
			if (userid >= 0) {
				getServletContext().log("-  Login -> login succeded for user " + userid + "(" + username + ")");
				HttpSession session = request.getSession();
				Player player = new Player(username, userid);
        			session.setAttribute("player", player);
			} else {        			
        			getServletContext().log("-  Login -> login failed, user is: " + userid + "(" + username + ")");
				viewName = "/Welcome.jsp";
			}
			
			RequestDispatcher d = getServletContext().getRequestDispatcher(viewName);

			if (d != null) {
				getServletContext().log("-  Login -> Dispatching to View" + viewName);
				d.forward(request, response);
			} else {
				getServletContext().log("-  Login -> No view named " + viewName);
			}
			
		} catch (Exception ex) {
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			String prettyException = "<br>" + sw.toString();
			prettyException = prettyException.replace("\n", "<br>");
			
			request.setAttribute("exception", prettyException);

			RequestDispatcher d = getServletContext().getRequestDispatcher(errorViewName);

			if (d != null) {
				getServletContext().log("-  Login -> Dispatching to errorView " + errorViewName);
				d.forward(request, response);
			} else {
				getServletContext().log("-  Login -> No errorView named " + errorViewName);
			}
		}

		getServletContext().log("-- Login -- Done");
	}
}


		
		
	

