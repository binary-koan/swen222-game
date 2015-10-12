package gui.renderer;

import game.*;

public abstract class Door implements Drawable {
	private Direction linkDirection;

	public Door(Direction linkDirection) {
		this.linkDirection = linkDirection;
	}

	public Direction getLinkDirection() {
		return linkDirection;
	}

	@Override
	public Direction getFacingDirection() {
		return linkDirection.opposite();
	}
}
