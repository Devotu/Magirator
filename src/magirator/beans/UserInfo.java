package magirator.beans;

import java.sql.ResultSet;

/* Keeping user info during session */
public class UserInfo {

	//Attributes
	private String name;
	private int id;
	
	//TODO remove
	public UserInfo () { }
	
	//TODO remove
	public UserInfo (String s, int i) {
		this.name = s;
		this.id = i;
	}
	
	public UserInfo (ResultSet rs) throws Exception{
		this.setId(rs.getInt("id(u)"));
		this.setName(rs.getString("u.name"));
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
