package game;

import java.util.*;

public class Chest extends Item{

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

	public String quiz() {
		return "Whats 2 + 5?";
	}

	public List<Item> getItems() {
		return chestItems;
	}
	
	public void openedChest() {
		hasOpened = true;
	}
	@Override
	public Direction getFacingDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingCube getBoundingCube() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
