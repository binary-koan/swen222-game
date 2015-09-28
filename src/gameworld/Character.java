package gameworld;

import game.Drawable;

public abstract class Character implements Drawable{

	protected int x;
	protected int y;
	
	public Character(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
