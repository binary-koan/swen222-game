package gameworld;

import java.util.HashMap;

public abstract class MoveableCharacter extends Character {

	protected HashMap<String, Item> inventory;
	
	public MoveableCharacter(int x, int y) {
		super(x, y);
		inventory = new HashMap<String, Item>();
	}
	
	

}
