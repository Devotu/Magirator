package magirator.dataobjects;

public class Tag {
	
	private int tagger;
	private int tagged;
	private String tag;
	private int polarity; //Range 1/-1
	
	public Tag(int tagger, int tagged, String tag, int polarity) {
		super();
		this.tagger = tagger;
		this.tagged = tagged;
		this.tag = tag;
		this.polarity = polarity;
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
