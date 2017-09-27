package magirator.data.entities;

import java.util.HashMap;
import java.util.Map;

public class Help {
	
	public enum sectionName { DASHBOARD, ADDDECK_COLORS, ADDDECK_DETAILS };	
	private Map<String, Boolean> sections = new HashMap<String, Boolean>();
	
	public Help(Map<String, Boolean> properties) {
		this.sections.put("dashboard", (Boolean) properties.get("dashboard"));
		this.sections.put("adddeck_colors", (Boolean) properties.get("adddeck_colors"));
		this.sections.put("adddeck_details", (Boolean) properties.get("adddeck_details"));
	}
	
	//To avoid sql injection vulnerability
	public static boolean isValidSection(String candidate){
		
	    for (sectionName sn : sectionName.values()) {
	        if (sn.name().equals(candidate.toUpperCase())) {
	            return true;
	        }
	    }

	    return false;
		
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
