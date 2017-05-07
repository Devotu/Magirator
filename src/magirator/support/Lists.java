package magirator.support;

import java.util.Collections;
import java.util.List;

import magirator.view.Opponent;

public class Lists {
	
	public static List<Opponent> mergeToOpponentList( List<Opponent> current, List<Opponent> additions){
		
		for (Opponent ao : additions) {
			if (current.contains(ao)) {
				
				int idx = current.indexOf(ao);
				Opponent co = current.get(idx);
				
				co.adjustRelevance( ao.getRelevance() );
				
				for (String l : ao.getLabels()) {
					co.addLabel(l);
				}
				
				co.addNumberOfGames(ao.getNumberOfGames());
				
				if (ao.getLastPlay().after(co.getLastPlay())) {
					co.setLastPlay(ao.getLastPlay());
				}
				
			} else {
				current.add(ao);
			}
		}
		
		return current;
	}
	
	public static List<Opponent> orderOpponents(List<Opponent> opponents){
		
		Collections.sort(opponents, new Opponent.OpponentCompare() );
		
		return opponents;
	}

}
