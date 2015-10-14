package gui.renderer;

import game.*;

public abstract class Door implements Drawable {
	private Direction linkDirection;
	private Room room;

	public Door(Room room, Direction linkDirection) {
		this.room = room;
		this.linkDirection = linkDirection;
	}

	public Direction getLinkDirection() {
		return linkDirection;
	}

	@Override
	public Direction getFacingDirection() {
		return linkDirection.opposite();
	}

	public Room getRoom() {
		return room;
	}
}
