package game;

import game.storage.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public abstract class Item implements Serializable {
	public enum Action {
		EXAMINE("Examine"), PICK_UP("Pick up"), SEARCH("Search"), SHOW_MENU("Other ..."), TAKE("Take");

		private final String text;

		/**
		 * Create a new action
		 * @param text string representation of the action
		 */
		Action(final String text) {
			this.text = text;
		}

		/* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
		@Override
		public String toString() {
			return text;
		}
	}

	public enum Color{
		BLUE, GREEN, YELLOW, RED;

		public static Color fromString(String color){
			if(color.equals("BLUE")){
	    		return BLUE;
	    	}
	    	else if(color.equals("GREEN")){
	    		return GREEN;
	    	}
	    	else if(color.equals("YELLOW")){
	    		return YELLOW;
	    	}
	    	else if(color.equals("RED")){
	    		return RED;
	    	}
	    	return BLUE;
		}
	}

	public Color getColor(){
		return this.color;
	}

	private String id;
	private String name;
	private String description;
	private String spriteName;
	private Color color;

	public Item(String id, String name, String description, String spriteName){
		this.id = id;
		this.name = name;
		this.description = description;
		this.spriteName = spriteName;
	}

	public String getID(){
    	return id;
    }

	public String getSpriteName() {
		return spriteName;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

    public List<Action> getAllowedActions() {
        List<Action> result = new ArrayList<>();
        result.add(Action.EXAMINE);
        return result;
    }



    public Element toXML() {
    	Element itemElement = new Element("item");
    	itemElement.addContent("id").setText(this.getID());
    	itemElement.addContent("name").setText(this.getName());
    	itemElement.addContent("description").setText(this.getDescription());
    	itemElement.addContent("spriteName").setText(this.getSpriteName());
    	itemElement.addContent("subclass").setText(this.getClass().toString());
    	if(this.color != null){
    		itemElement.addContent("color").setText(this.color.toString());
    	}
    	return itemElement;
    }

    public void loadXML(Game game, Element objectElement){
    	this.name = objectElement.getChildText("name");
    	this.description = objectElement.getChildText("description");
    	this.spriteName = objectElement.getChildText("spriteName");

    	if(objectElement.getChildText("color") != null){
    		this.color = Color.fromString(objectElement.getChildText("color"));
    	}
    }
}

