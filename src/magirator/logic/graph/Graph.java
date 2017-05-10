package magirator.logic.graph;

import java.util.ArrayList;
import java.util.List;

import magirator.data.collections.GameBundle;
import magirator.data.entities.Deck;
import magirator.support.Constants;

public class Graph {
	
	private String title;
	private Axis xAxis;
	private Axis yAxis;
	private Background background;
	private Content content;
	
	
	protected Graph(String title){
		this.title = title;
		this.xAxis = new Axis("x", 0, 1);
		this.yAxis = new Axis("y", 0, 1);
		this.background = new Background("red");
		this.content = new Content();
	}
	
	protected void generateWinrateGraph(List<GameBundle> games){
		this.xAxis = new Axis("percent", 0, 100);
		this.yAxis = new Axis("games", 0, games.size());
		
		float numberOfGames = 0;
		float wins = 0;
		List<Point> path = new ArrayList<>();
		
		for (GameBundle g : games) {
			if (g.getSelf().getResult().getPlace() == 1) {
				wins++;				
			}
			numberOfGames++;
			path.add(new Point(numberOfGames, (wins/numberOfGames)*100 ) );
		}
		
		this.content.addPath(path);
		
		this.background = new Background("red");
	}

	protected void generateColorbarsGraph(List<GameBundle> games) {
		this.yAxis = new Axis("colors", 0, Constants.numberOfMagicColors);
		
		int[] colorvalues = new int[Constants.numberOfMagicColors];
		
		for (GameBundle g : games) {
			Deck d = g.getSelf().getDeck();
			
			if (d.getBlackCards() > 0) { colorvalues[0]++; }
			if (d.getWhiteCards() > 0) { colorvalues[1]++; }
			if (d.getRedCards() > 0) { colorvalues[2]++; }
			if (d.getGreenCards() > 0) { colorvalues[3]++; }
			if (d.getBlueCards() > 0) { colorvalues[4]++; }
			if (d.getColorlessCards() > 0) { colorvalues[5]++; }
		}
		
		this.content.addBar(new Bar("black", colorvalues[0]));
		this.content.addBar(new Bar("white", colorvalues[1]));
		this.content.addBar(new Bar("red", colorvalues[2]));
		this.content.addBar(new Bar("green", colorvalues[3]));
		this.content.addBar(new Bar("blue", colorvalues[4]));
		this.content.addBar(new Bar("colorless", colorvalues[5]));
		
		int xMax = 0;
		for (int i : colorvalues) {
			if (i > xMax) {
				xMax = i;
			}
		}

		this.xAxis = new Axis("used", 0, xMax);		
	}
}
