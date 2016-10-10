package magirator.beans;

/* Short opponent info */
public class ListOpponent {

	//Attributes
	private String displayname;
	private int id;
	
	public ListOpponent () { }
	
	
	//Setters
	public void setDisplayname(String s){
		this.displayname = s;
	}
	
	public void setId(int i){
		this.id = i;
	}
	
	//Getters	
	public String getDisplayname(){
		return this.displayname;
	}
	
	public int getId(){
		return this.id;
	}
}
