package gui.renderer;

import game.Direction;
import game.Key;
import game.Room;

/**
 * Represents a visible door (a door leading through a wall)
 *
 * @author Jono Mingard
 */
public class VisibleDoor extends Door {
    private String spriteName;

    /**
     * Create a new visible door
     *
     * @param room room the door is contained in
     * @param linkDirection direction the door points
     * @param color colour to draw the door (should be the colour of the room the door points to)
     */
	public VisibleDoor(Room room, Direction linkDirection, Key.Color color) {
		super(room, linkDirection);

        if (color == null) {
            spriteName = "objects/door.png";
        }
        else {
            spriteName = "objects/door-" + color.toString().toLowerCase() + ".png";
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
