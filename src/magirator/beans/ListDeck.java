package magirator.beans;

/* Short deck info */
public class ListDeck {

	//Attributes
	private String displayname;
	private int id;
	private float wins;
	private String format;
	
	public ListDeck () { }
	
	public ListDeck (String s, int i, float f, String ss) {
		this.displayname = s;
		this.id = i;
		this.wins = f;
		this.format = ss;
	}	
	
	//Setters
	public void setDisplayname(String s){
		this.displayname = s;
	}
	
	public void setId(int i){
		this.id = i;
	}
	
	public void setWins(int f){
		this.wins = f;
	}

	public void setFormat(String ss){
		this.format = ss;
	}
	
	//Getters	
	public String getDisplayname(){
		return this.displayname;
	}
	
	public int getId(){
		return this.id;
	}
	
	public float getWins(){
		return this.wins;
	}

	public String getFormat(){
		return this.format;
	}
}
