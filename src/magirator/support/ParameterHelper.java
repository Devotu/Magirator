package magirator.support;

import javax.servlet.http.HttpServletRequest;

public class ParameterHelper {
	
	public static String returnParameter(HttpServletRequest req, String name, String def){
		
		String param = req.getParameter(name);
			
		if (param != null){
			return param; 
		}
		
		return def;
	}
	
	public static int returnParameter(HttpServletRequest req, String name, int def){
		
		String param = req.getParameter(name);
		
		if (param != null){
			
			int value = Integer.valueOf( req.getParameter(name) );
			
			if (value >= 0){
				return value; 
			}
		}
		
		return def;
	}

}
