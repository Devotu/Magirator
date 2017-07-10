package magirator.data.entities;

import java.util.Date;
import java.util.Map;

public class Reset {
	
	private String code;
	private Date created;
	
	public Reset(Map<String, ?> properties) {
		this.code = (String)properties.get("code");
		
		long longTime = (Long)properties.get("created");
		this.created = new Date(longTime);
	}

	public String getCode() {
		return code;
	}

	public Date getCreated() {
		return created;
	}
}
