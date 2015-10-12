package gui.renderer;

import game.*;
import org.jdom2.Element;

public class Door implements Drawable {
	private Room targetRoom;

	public Door(Room targetRoom) {
		this.targetRoom = targetRoom;
	}

	public Room getTargetRoom() {
		return targetRoom;
	}

	@Override
	public String getName() {
		return "Door";
	}

	@Override
	public Direction getFacingDirection() {
		return null;
	}

	@Override
	public Point3D getPosition() {
		return new Point3D(0, 0, 0);
	}

	@Override
	public String getSpriteName() {
		return null;
	}
}
