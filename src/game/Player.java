package game;

import game.storage.Serializable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Represents a player's avatar in the game
 */
public class Player implements Drawable, Serializable {
	private String name;
	private String spriteName;
    private Room room;
    private Direction facingDirection;
    private Item heldItem;

    /**
     * Create a new player
     *
     * @param name player name
     * @param spriteName sprite used to render the player
     * @param room room the player is initially in
     */
    public Player(String name, String spriteName, Room room) {
    	this.name = name;
    	this.spriteName = spriteName;
        this.room = room;
        this.facingDirection = Direction.NORTH;

        room.addPlayer(this);
    }

    // Getters

    /**
     * @return the name of the player
     */
    public String getName() {
    	return name;
    }

    /**
     * @return the room that the player is in
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @return the direction the player is facing
     */
    public Direction getFacingDirection() {
        return facingDirection;
    }

    /**
     * @return the item the player is holding
     */
    public Item getHeldItem() {
        return heldItem;
    }

    /**
     * @return a point near the center of the wall the player is viewing their room from
     */
    @Override
    public Point3D getPosition() {
        switch (this.facingDirection) {
            case NORTH:
                return new Point3D(160, 0, 10);
            case SOUTH:
                return new Point3D(160, 0, 310);
            case EAST:
                return new Point3D(310, 0, 160);
            case WEST:
            default:
                return new Point3D(10, 0, 160);
        }
    }

    @Override
    public String getSpriteName() {
        return spriteName;
    }

    // Actions

    /**
     * Set the player's facing direction
     *
     * @param facingDirection new direction the player should face
     */
    public void turn(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    /**
     * Move to the adjacent room in the given direction
     *
     * @param movementDirection direction to move in
     * @return true if the move succeeded, false otherwise
     */
	public boolean move(Direction movementDirection) {
		Room newRoom = room.getConnection(movementDirection);

		if (newRoom == null) {
			return false;
		}
		else {
			room.removePlayer(this);
			newRoom.addPlayer(this);
			room = newRoom;
            return true;
		}
	}

    /**
     * Set the item the player is holding
     *
     * @return true if the item was picked up, false otherwise (eg. if the player was already holding an item)
     */
    public boolean pickUp(Item item) {
        if (item == null || heldItem != null) {
            return false;
        }
        else {
            heldItem = item;
            return true;
        }
    }

    // Serialization

	@Override
	public Element toXML() {
		Element player = new Element("player");
		player.addContent("name").setText(this.name);
		player.addContent("spriteName").setText(this.name);
		player.addContent("room").setText(this.room.getID());
		player.addContent("facingDirection").setText(this.facingDirection.toString());
		player.addContent("heldItem").setText(heldItem.getID());
		return player;
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		this.name = objectElement.getChildText("name");
		this.spriteName = objectElement.getChildText("spriteName");
		this.room = game.getRoom(objectElement.getChildText("room"));
		this.facingDirection = Direction.fromString(objectElement.getChildText("facingDirectiom"));
		this.heldItem = game.getItem(objectElement.getChildText("heldItem"));
	}

}