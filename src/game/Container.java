package game;

import game.storage.GameData;

import java.util.*;

public class Container extends Item implements Pickable {
	private List<Item> chestItems;
	private boolean hasOpened;

	public Container(String name, String description, String spriteName, Item...items) {
		super(name, description, spriteName);
		this.chestItems = new ArrayList<>();
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
    public List<Action> getAllowedActions() {
        List<Action> list = super.getAllowedActions();
        list.add(0, Action.SEARCH);
        list.add(0, Action.PICK_UP); //TODO remove (this one is just for testing)
        return list;
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
