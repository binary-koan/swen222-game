package gui.renderer;

import game.Direction;
import game.Key;

public class VisibleDoor extends Door {
    private String spriteName;

	public VisibleDoor(Direction linkDirection, Key.Color color) {
		super(linkDirection);

        if (color == null) {
            spriteName = "objects/door.png";
        }
        else {
            spriteName = "object/door-" + color.toString().toLowerCase() + ".png";
        }
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
		return spriteName;
	}
}
