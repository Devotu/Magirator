package magirator.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import magirator.data.interfaces.IPlayer;

public class Opponent implements IPlayer {
	
	private int id;
	private String name;
	private int relevance;
	private List<String> labels;
	private int numberOfGames;
	private Date lastPlay;
	
	
	public Opponent(IPlayer ip) {
		this.id = ip.getId();
		this.name = ip.getName();
		this.relevance = 0;
		this.labels = new ArrayList<String>();
		this.numberOfGames = 0;
		this.lastPlay = null;
	}

	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public int getRelevance() {
		return this.relevance;
	}
	
	public List<String> getLabels() {
		return labels;
	}

	public int getNumberOfGames() {
		return numberOfGames;
	}

	public Date getLastPlay() {
		return lastPlay;
	}


	
	public void adjustRelevance(int points){
		this.relevance += points;
	}
	
	public void addLabel(String label){
		if (!this.labels.contains(label)){
			this.labels.add(label);
		}
	}
	
	public void addOneGame(){
		this.numberOfGames++;
	}
	
	public void addNumberOfGames(int games){
		this.numberOfGames += games;
	}
	
	public void setLastPlay(Date newDate){
		this.lastPlay = newDate;
	}
	
	
	
	public boolean equalsOpponent(Opponent o){
		return o.id == this.id;
	}



	public static class OpponentCompare implements Comparator<Opponent> {
	    @Override
	    public int compare(Opponent o1, Opponent o2) {
	        return Integer.valueOf(o2.getRelevance()).compareTo(o1.getRelevance());
	    }
	}

}
