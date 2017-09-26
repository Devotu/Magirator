package magirator.data.entities;

import java.util.Map;

public class Help {
	
	private boolean dashboard;
	private boolean adddeck_colors;
	private boolean adddeck_details;
	
	public Help(Map<String, ?> properties) {
		this.dashboard = (Boolean) properties.get("dashboard");
		this.adddeck_colors = (Boolean) properties.get("adddeck_colors");
		this.adddeck_details = (Boolean) properties.get("adddeck_details");
	}
	
	/** @return :Help { [section:true] } 
	 * All sections are set to true when created
	 */
	public static String neoCreator() {
		return ":Help { "
				+ "dashboard: true,"
				+ "adddeck_colors: true,"
				+ "adddeck_details: true"
				+ "} ";
	}

}
