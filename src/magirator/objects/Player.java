package magirator.objects;

import java.util.Map;

public class Player {
	
	private String name;
	private int id;

	public Player(int id, Map playerMap) {
		this.id = id;
		this.name = (String) playerMap.get("name");
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
