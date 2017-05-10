package magirator.logic.graph;

import java.util.ArrayList;
import java.util.List;

public class Content {

	private List<List<Point>> paths;
	private List<Bar> bars;
		
	public Content() {
		this.paths = new ArrayList<>();
		this.bars = new ArrayList<>();
	}

	public void addPath(List<Point> path){
		this.paths.add(path);
	}
	
	public void addBar(Bar bar){
		this.bars.add(bar);
	}
}
