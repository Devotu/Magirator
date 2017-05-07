package magirator.data.entities;

import java.util.Date;
import java.util.Map;

public class Reset {
	
	private String code;
	private Date created;
	
	public Reset(Map resetMap) {
		this.code = (String)resetMap.get("code");
		long longTime = (Long)resetMap.get("created");
		this.created = new Date(longTime);
	}

	public String getCode() {
		return code;
	}

	public Date getCreated() {
		return created;
	}
}
