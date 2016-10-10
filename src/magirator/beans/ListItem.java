package magirator.beans;

/* Class must be used in all classes displayed in a generic list */
public class ListItem {

	//Attributes
	private String displayname;
	
	public ListItem () { }
	
	
	//Setters
	public void setDisplayname(String s){
		this.displayname = s;
	}
	
	//Getters	
	public String getDisplayname(){
		return this.displayname;
	}
}
