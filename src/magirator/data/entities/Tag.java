package magirator.data.entities;

import java.util.Map;

public class Tag {
	
	private int tagger;
	private int tagged;
	private String text;
	private int polarity; //Range 1/-1
	
	public Tag(int tagger, int tagged, String text, int polarity) {
		this.tagger = tagger;
		this.tagged = tagged;
		this.text = text;
		this.polarity = polarity;
	}
	
	public Tag(int tagger, int tagged, Map<String, ?> properties) {
		this.tagger = tagger;
		this.tagged = tagged;
		this.text = (String)properties.get("tag");
		this.polarity = (int)(long)properties.get("polarity");
	}

	public int getTagger() {
		return tagger;
	}
	public int getTagged() {
		return tagged;
	}
	public String getText() {
		return text;
	}
	public int getPolarity() {
		return polarity;
	}
	
	
	/** @return :Tag {id:?, polarity:?, text:?}
	 */
	public static String neoCreator() {
		return ":Tag {id:?, polarity:?, text:?}";
	}
	
	/** Special creator to be used on merges as the unique id will be treated as a new entity
	 *  @return :Tag {polarity:?, text:?}
	 */
	public static String neoMerge() {
		return ":Tag {polarity:?, text:?}";
	}
}
