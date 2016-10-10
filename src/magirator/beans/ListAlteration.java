package magirator.beans;

import java.util.Date;

/* Short alteration info */
public class ListAlteration extends ListItem {

	//Attributes
	private String displayname;
	private int id;
	private Date date;
	
	public ListAlteration () { }
	
	//Setters
	public void setDisplayname(String s){
		this.displayname = this.displayname;
	}
	
	public void setId(int i){
		this.id = i;
	}
	
	public void setDate(Date d){
		this.date = d;
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
	
}
