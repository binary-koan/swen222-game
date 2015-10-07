package game;

import game.storage.Serializable;

import java.util.ArrayList;
import java.util.List;

public abstract class Item implements Serializable {
	public enum Action {
		PICK_UP("Pick up"), SEARCH("Search");

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

	public class IllegalActionException extends Exception { }

	private String name;
	private String description;
	private String spriteName;

	public Item(String name, String description, String spriteName){
		this.name = name;
		this.description = description;
		this.spriteName = spriteName;
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

	public abstract List<Action> getAllowedActions();
	public abstract void performAction(Action action, Player player);
}
