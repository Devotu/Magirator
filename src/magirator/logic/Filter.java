package magirator.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import magirator.data.collections.GameBundle;
import magirator.data.collections.Participant;
import magirator.data.entities.Game;
import magirator.view.Condition;

public class Filter {
	
	public static boolean gameHasCondition(GameBundle bundle, Condition condition){
		
		//Select targets containing information asked to filter on
		List<Filterable> subjects = new ArrayList<>();
				
		switch (condition.getCondition()) {
		case "against":
			List <Participant> opponents = bundle.getOpponents();
			subjects = opponents.stream().map(o -> new Filterable(o)).collect(Collectors.toList());
			break;

		case "with":
			Participant self = bundle.getSelf();
			subjects.add(new Filterable(self));
			break;			

		case "after":
			Game game = bundle.getGame();
			subjects.add(new Filterable(game));
			break;

		default:
			break;
		}
		
		
		//Select targets containing information asked to filter on
		List<String> values = new ArrayList<>();
		
		switch (condition.getSubject()) {
		case "player":
			values = subjects.stream()
				.filter( s -> s.playerIdExist() )
				.map( s -> String.valueOf( s.getPlayerId() ) )
				.collect( Collectors.toList() );
			break;			

		case "format":
			values = subjects.stream()
			.filter( s -> s.formatExist() )
			.map( s -> String.valueOf( s.getFormat() ) )
			.collect( Collectors.toList() );
			break;			

		case "date":
			values = subjects.stream()
			.filter( s -> s.dateExist() )
			.map( s -> String.valueOf( s.getDate() ) )
			.collect( Collectors.toList() );
			break;

		default:
			break;
		}
		
		
		//Does any of those containing that property contain the requested value?
		return values.stream().filter(v -> v.equals(condition.getValue())).findFirst().isPresent();
	}

}