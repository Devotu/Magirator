package magirator.logic.graph;

import java.util.ArrayList;
import java.util.List;

import magirator.data.collections.GameBundle;

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
		
		float game = 0;
		float wins = 0;
		List<Point> path = new ArrayList<>();
		
		for (GameBundle g : games) {
			if (g.getSelf().getResult().getPlace() == 1) {
				wins++;				
			}
			game++;
			path.add(new Point(game, (wins/game)*100 ) );
		}
		
		this.content.addPath(path);
		
		this.background = new Background("red");
	}
	
	protected void generateColorBarGraph(List<GameBundle> games){
		this.yAxis = new Axis("Percent", 0, games.size());
	}
}
