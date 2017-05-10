package magirator.restlets;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class API extends Application{

	@Override
	public Restlet getInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/graph", GenerateGraph.class);
		return router;
	}
}
