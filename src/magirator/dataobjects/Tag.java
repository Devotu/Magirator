package magirator.dataobjects;

import java.util.Map;

public class Tag {
	
	private int tagger;
	private int tagged;
	private String tag;
	private int polarity; //Range 1/-1
	
	public Tag(int tagger, int tagged, String tag, int polarity) {
		this.tagger = tagger;
		this.tagged = tagged;
		this.tag = tag;
		this.polarity = polarity;
	}
	
	public Tag(int tagger, int tagged, Map properties) {
		this.tagger = tagger;
		this.tagged = tagged;
		this.tag = (String)properties.get("tag");
		this.polarity = (int)(long)properties.get("polarity");
	}

	public int getTagger() {
		return tagger;
	}
	public int getTagged() {
		return tagged;
	}
	public String getTag() {
		return tag;
	}
	public int getPolarity() {
		return polarity;
	}

}
