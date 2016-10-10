package magirator.control;

import javax.servlet.*;

public class MagiratorServlet extends GenericServlet {

	public void service(ServletRequest request, ServletResponse response) throws java.io.IOException, ServletException{
		getServletContext().log("-- MagiratorServlet --");

		String[] controllers = request.getParameterValues("controller");
		String viewName = request.getParameter("goView");
		String[] altControllers = request.getParameterValues("altController");
		String altViewName = request.getParameter("altView");
		String errorViewName = request.getParameter("errorView");
		
		String action = request.getParameter("action");
		if ( action == null ) {
			action = "go";
		}

		if ( viewName == null ){
			getServletContext().log("MagiratorServlet -- No view found directing to Welcome.jsp");
			viewName = "/Welcome.jsp";
		}
		

		try {
			if ( action.equals("go") ){
				if(controllers == null){
					getServletContext().log("MagiratorServlet -- No controllers passed ");
				} else {
					for (int i=0; i < controllers.length; i++) {
						RequestDispatcher d = getServletContext().getRequestDispatcher(controllers[i]);
						if (d != null) {
							d.include(request, response);
							getServletContext().log("MagiratorServlet -- invoking " + controllers[i]);
						} else {
							getServletContext().log("MagiratorServlet -- No controller named " + controllers[i]);
						}
					}
				}			
			
				RequestDispatcher d = getServletContext().getRequestDispatcher(viewName);				
				if (d != null) {
					getServletContext().log("MagiratorServlet -- Dispatching to View" + viewName);
					d.forward(request, response);
				} else { getServletContext().log("MagiratorServlet -- No view named " + viewName); }
				
			} else {
				if(altControllers == null){
					getServletContext().log("MagiratorServlet -- No altControllers passed ");
				} else {
					for (int i=0; i < controllers.length; i++) {
						RequestDispatcher d = getServletContext().getRequestDispatcher(altControllers[i]);
						if (d != null) {
							d.include(request, response);
							getServletContext().log("MagiratorServlet -- invoking altController " + altControllers[i]);
						} else {
							getServletContext().log("MagiratorServlet -- No controller named " + altControllers[i]);
						}
					}
				}
				RequestDispatcher d = getServletContext().getRequestDispatcher(altViewName);				
				if (d != null) {
					getServletContext().log("MagiratorServlet -- Dispatching to altView" + altViewName);
					d.forward(request, response);
				} else { getServletContext().log("MagiratorServlet -- No view named " + altViewName); }
			}

		} catch (Exception ex) {
			request.setAttribute("exception", ex.toString());
			getServletContext().log("MagiratorServlet -- This is the error " + ex.toString());

			RequestDispatcher d = getServletContext().getRequestDispatcher(errorViewName);

			if (d != null) {
				getServletContext().log("MagiratorServlet -- Dispatching to errorView " + errorViewName);
				d.forward(request, response);
			} else { getServletContext().log("MagiratorServlet -- No errorView named " + errorViewName); }
		}
	}
}