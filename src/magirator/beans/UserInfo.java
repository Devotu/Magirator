package magirator.beans;

/* Keeping user info during session */
public class UserInfo {

	//Attributes
	private String name;
	private int id;
	
	public UserInfo () { }
	
	public UserInfo (String s, int i) {
		this.name = s;
		this.id = i;
	}
	
	//Setters
	public void setName(String s){
		this.name = s;
	}
	
	public void setId(int i){
		this.id = i;
	}
	
	//Getters	
	public String getName(){
		return this.name;
	}
	
	public int getId(){
		return this.id;
	}
}
