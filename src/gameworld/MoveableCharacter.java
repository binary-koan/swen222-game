package gameworld;

import java.util.HashMap;

import game.Direction;

public abstract class MoveableCharacter extends Character {

	protected Direction MoveableDirection;
	protected HashMap<String, Item> inventory;
	
	public MoveableCharacter(int x, int y, Direction d) {
		super(x, y);
		this.MoveableDirection = d;
		inventory = new HashMap<String, Item>();
	}
	
	

}
