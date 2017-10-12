package magirator.data.collections;

import magirator.data.entities.Help;
import magirator.data.entities.Settings;

public class SettingsBundle {
	
	private Settings settings;
	private Help help;
	
	public SettingsBundle(Settings settings, Help help){
		this.settings = settings;
		this.help = help;
	}
}
