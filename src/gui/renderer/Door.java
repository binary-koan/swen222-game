package gui.renderer;

import game.*;

/**
 * Represents a door - a drawable object which links to another room
 *
 * @author Jono Mingard
 */
public abstract class Door implements Drawable {
	private Direction linkDirection;
	private Room room;

	/**
	 * Create a new door
	 *
	 * @param room the room the door is contained in
	 * @param linkDirection direction the door points (ie. if it links to a room north of the current one, this should
     *                      be {@link Direction#NORTH})
	 */
	public Door(Room room, Direction linkDirection) {
		this.room = room;
		this.linkDirection = linkDirection;
	}

    /**
     * Returns the direction of the room this door points to
     *
     * @return direction the door points
     */
	public Direction getLinkDirection() {
		return linkDirection;
	}

    /**
     * Returns the room this door is contained in (not the room it points to)
     *
     * @return the room the door is contained in
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Returns the opposite direction to {@link #getLinkDirection()} since doors should face away from the room they
     * point to
     *
     * @return the direction this door should face
     */
	@Override
	public Direction getFacingDirection() {
		return linkDirection.opposite();
	}
}
