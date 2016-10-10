package magirator.beans;

import java.util.Date;

/* Short game info */
public class ListGame {

	//Attributes
	private String displayname;
	private int id;
	private Date date;
	private boolean win;
	
	public ListGame () { }
	
	
	//Setters
	public void setDisplayname(String s){
		this.displayname = s;
	}
		
	public void setId(int i){
		this.id = i;
	}
	
	public void setDate(Date d){
		this.date = d;
	}
	
	public void setWin(boolean b){
		this.win = b;
	}
	
	//Getters
	public String getDisplayname(){
		return this.displayname;
	}

	public int getId(){
		return this.id;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public boolean getWin(){
		return this.win;
	}
}
