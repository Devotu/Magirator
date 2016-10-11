package magirator.beans;

import java.util.HashMap;

/* Class must be used in all classes displayed in a generic list */
public class ListItem {

	//Attributes
	private int id;	
	private String displayname;	
	private HashMap sortables;
	private HashMap filterables;
	
	public ListItem () { }	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	//Setters
	public void setDisplayname(String s){
		this.displayname = s;
	}
	
	//Getters	
	public String getDisplayname(){
		return this.displayname;
	}


	public HashMap getSortables() {
		return sortables;
	}


	public void setSortables(HashMap sortables) {
		this.sortables = sortables;
	}


	public HashMap getFilterables() {
		return filterables;
	}


	public void setFilterables(HashMap filterables) {
		this.filterables = filterables;
	}
	
	
}
