package game;

import storage.Serializable;

import org.jdom2.Element;

public abstract class Item implements Serializable {

	private String id;
	private String name;
	private String description;
	private String spriteName;

	public Item(String id, String name, String description, String spriteName){
		this.id = id;
		this.name = name;
		this.description = description;
		this.spriteName = spriteName;
	}

	public String getID() {
    	return id;
    }

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getSpriteName() {
		return spriteName;
	}

	/**
	 * {@inheritDoc}
	 * @author holdawscot
	 */
	@Override
    public Element toXML() {
    	Element itemElement = new Element("item");
    	itemElement.addContent(new Element("id").setText(this.getID()));
    	itemElement.addContent(new Element("name").setText(this.getName()));
    	itemElement.addContent(new Element("description").setText(this.getDescription()));
    	itemElement.addContent(new Element("spriteName").setText(this.getSpriteName()));
    	itemElement.addContent(new Element("subClass").setText(this.getClass().toString()));
    	return itemElement;
    }

	/**
	 * {@inheritDoc}
	 * @author holdawscot
	 */
	@Override
    public void loadXML(Game game, Element objectElement){
    	this.name = objectElement.getChildText("name");
    	this.description = objectElement.getChildText("description");
    	this.spriteName = objectElement.getChildText("spriteName");
    }
}
