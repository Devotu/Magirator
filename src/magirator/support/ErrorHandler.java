package magirator.support;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorHandler {
	
	public static String printStackTrace(final Throwable throwable) {
     	final StringWriter sw = new StringWriter();
     	final PrintWriter pw = new PrintWriter(sw, true);
     	throwable.printStackTrace(pw);
     	return sw.getBuffer().toString();
	}

}
