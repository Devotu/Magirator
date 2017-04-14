package magirator.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import magirator.data.collections.IPlayerGame;
import magirator.data.objects.Game;
import magirator.data.objects.Minion;
import magirator.viewobjects.Opponent;

public class Ranker {
	
	public static List<Opponent> rankMinions(List<Minion> minions){
		
		List<Opponent> minionOpponents = new ArrayList<>();
		
		for (Minion m : minions){
			Opponent o = new Opponent(m);
			o.addLabel("Minion");
			o.adjustRelevance(-50);
			minionOpponents.add(o);
		}
		
		return minionOpponents;
	}

	public static List<Opponent> rankPrevious(List<IPlayerGame> previous) {
		
		List<Opponent> pos = new ArrayList<>();
		Date today = new Date();
		
		//Merge list
		for (IPlayerGame ipg : previous){
			
			Opponent o = new Opponent(ipg.getPlayer());
			Game g = ipg.getGame();
			
			boolean found = false;
			
			for (Opponent po : pos) {
				
				if (po.equalsOpponent(o)){
					
					found = true;
					
					po.addOneGame();
					
					if (g.getCreated().after(po.getLastPlay())) {
						po.setLastPlay(g.getCreated());					
					}
					
					break;					
				}
			}
			
			if (!found) {
				o.addLabel("Previous");
				o.addOneGame();
				o.setLastPlay(ipg.getGame().getCreated());
				pos.add(o);
			}
		}
		
		//Evaluate Opponents
		for (Opponent o : pos){
			
			//Previously played
			o.adjustRelevance(1000);
			
			//Number of games
			o.adjustRelevance( (o.getNumberOfGames() * 10) );
			
			//Last played			
			long diff = 0;
			long lLastPlayed = o.getLastPlay().getTime();
			long ltoday = today.getTime();
			
			diff = ltoday - lLastPlayed;
			
			long daydiff = diff / (1000*60*60*24);
			
			if (daydiff > 365) { daydiff = 365; }
			
			int recent = Math.toIntExact(365 - daydiff);
			
			o.adjustRelevance( recent );
		}
		
		return pos;
	}

}
