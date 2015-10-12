package gui.renderer;

import game.Direction;
import game.Room;

public class VisibleDoor extends Door {
	public VisibleDoor(Direction facingDirection) {
		super(facingDirection);
    }

    @Override
    public String getName() {
        return "Door";
    }

    @Override
    public Point3D getPosition() {
        if (getFacingDirection() == Direction.NORTH) {
            return new Point3D(160, -10, 0);
        }
        else if (getFacingDirection() == Direction.SOUTH) {
            return new Point3D(160, -10, 320);
        }
        else if (getFacingDirection() == Direction.EAST) {
            return new Point3D(0, -10, 160);
        }
        else {
            return new Point3D(320, -10, 160);
        }
    }

	@Override
	public String getSpriteName() {
		return "objects/door.png";
	}
}
