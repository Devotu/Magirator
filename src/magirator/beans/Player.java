package magirator.beans;

import java.util.Map;

public class Player {
	
	private String name;

	public Player(Map playerMap) {
		this.name = (String) playerMap.get("name");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
