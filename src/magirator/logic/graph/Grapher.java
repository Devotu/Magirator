package magirator.logic.graph;

import java.util.List;

import magirator.data.collections.GameBundle;

public class Grapher {

	private String title;
	private List<GameBundle> games;
	
	public Grapher(String title, List<GameBundle> games){
		this.title = title;
		this.games = games;
	}
	
	public Graph generateWinrateGraph(){
		Graph g = new Graph(this.title);
		
		g.generateWinrateGraph(this.games);
		
		return g;
	}
}
