package game;

import game.storage.GameData;

import java.util.*;

public class Chest extends Item {
	private List<Item> chestItems;
	private boolean hasOpened;

	public Chest(String name, String spriteName, Item...items) {
		super(name, spriteName, false);
		this.chestItems = new ArrayList<Item>();
		int j = 0;
		for(Item i : items) {
			if (j <= 3) {
				this.chestItems.add(i);
			}
			j++;
		}
		this.hasOpened = false;
	}

	public String mathQuiz() {
		return "Whats 2 + 5?";
	}

	public List<Item> getItems() {
		return chestItems;
	}

	public void openChest() {
		hasOpened = true;
	}

	@Override
	public void toXML() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object loadXML(GameData gameData) {
		// TODO Auto-generated method stub
		return null;
	}

}