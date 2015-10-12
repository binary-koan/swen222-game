package gui.renderer;

import game.*;
import org.jdom2.Element;

public abstract class Door implements Drawable {
	private Direction facingDirection;

	public Door(Direction facingDirection) {
		this.facingDirection = facingDirection;
	}

	@Override
	public Direction getFacingDirection() {
		return facingDirection;
	}
}
