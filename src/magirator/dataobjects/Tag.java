package magirator.dataobjects;

public class Tag {
	
	private int tagger;
	private int tagged;
	private String tag;
	
	public Tag(int tagger, int tagged, String tag) {
		super();
		this.tagger = tagger;
		this.tagged = tagged;
		this.tag = tag;
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

}
