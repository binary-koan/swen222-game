package game;

import game.storage.Serializable;

import java.util.ArrayList;
import java.util.List;

public abstract class Item implements Serializable {
	public enum Action {
		EXAMINE("Examine"), PICK_UP("Pick up"), SEARCH("Search"), SHOW_MENU("Other ...");

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
}

