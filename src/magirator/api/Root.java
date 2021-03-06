package magirator.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import magirator.api.live.AddTag;
import magirator.api.live.AdminCancelGame;
import magirator.api.live.AlterLife;
import magirator.api.live.DeclareDead;
import magirator.api.live.GameStatus;
import magirator.api.live.GetGameId;
import magirator.api.live.GetGameTags;
import magirator.api.live.GetPreviousTags;
import magirator.api.live.IsAdmin;
import magirator.api.live.JoinGame;
import magirator.api.live.RemoveTag;
import magirator.api.live.StartNewGame;

public class Root extends Application{

	@Override
	public Restlet getInboundRoot() {
		Router router = new Router(getContext());
		
		//Graph
		router.attach("/graphoptions", GraphOptions.class);
		router.attach("/graph", GenerateGraph.class);
		
		//Game
		router.attach("/gamestatus", GameStatus.class);
		router.attach("/startnewgame", StartNewGame.class);
		router.attach("/playerlivegameid", GetGameId.class);
		router.attach("/alterlife", AlterLife.class);
		router.attach("/isadmin", IsAdmin.class);
		router.attach("/cancelgame", AdminCancelGame.class);
		router.attach("/joingame", JoinGame.class);	
		router.attach("/declaredead", DeclareDead.class);
		router.attach("/addtag", AddTag.class);
		router.attach("/previoustags", GetPreviousTags.class);
		router.attach("/tags", GetGameTags.class);
		router.attach("/removetag", RemoveTag.class);
		
		
		//General
		
		return router;
	}
}
