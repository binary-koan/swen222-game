package gui.renderer;

import game.Direction;
import game.Room;

public class VisibleDoor extends Door {
    private Direction facingDirection;

	public VisibleDoor(Room targetRoom, Direction facingDirection) {
		super(targetRoom);

        this.facingDirection = facingDirection;
    }

    @Override
    public Direction getFacingDirection() {
        return facingDirection;
    }

    @Override
    public Point3D getPosition() {
        if (facingDirection == Direction.NORTH) {
            return new Point3D(160, -10, 0);
        }
        else if (facingDirection == Direction.SOUTH) {
            return new Point3D(160, -10, 320);
        }
        else if (facingDirection == Direction.EAST) {
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
