package gui.renderer;

import game.Direction;
import game.Room;

/**
 * An invisible door (ie. a passage along a corridor)
 *
 * @author Jono Mingard
 */
public class InvisibleDoor extends Door {
    /**
     * Create a new invisible door
     *
     * @param room room the door is contained in
     * @param linkDirection direction the door points
     */
    public InvisibleDoor(Room room, Direction linkDirection) {
        super(room, linkDirection);
    }

    @Override
    public String getName() {
        return "Passage";
    }

    @Override
    public Point3D getPosition() {
        return null;
    }

    @Override
    public String getSpriteName() {
        return null;
    }
}
