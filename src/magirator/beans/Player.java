package magirator.beans;

import java.util.Map;

public class Player {
	
	private String name;
	private int id;

	public Player(Map playerMap) {
		this.name = (String) playerMap.get("name");
		this.name = (String) playerMap.get("id");
	}

	public Player(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId(){
		return this.id;
	}	

	public void setId(int i){
		this.id = i;
	}

}
