package magirator.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import magirator.api.live.StartNewGame;

public class Root extends Application{

	@Override
	public Restlet getInboundRoot() {
		Router router = new Router(getContext());
		//Graph
		router.attach("/graphoptions", GraphOptions.class);
		router.attach("/graph", GenerateGraph.class);
		
		//Game
		router.attach("/gamestatus", CurrentGameStatus.class);
		router.attach("/startnewgame", StartNewGame.class);
		
		return router;
	}
}
