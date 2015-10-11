package game;

import game.storage.Serializable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Player implements Drawable, Serializable{
	public class InvalidMovementException extends Exception { }

	private String name;
	private String spriteName;
    private Room room;
    private Direction facingDirection;
    private Item heldItem;

    public Player(String name, String spriteName, Room room) {
    	this.name = name;
    	this.spriteName = spriteName;
        this.room = room;
        room.addPlayer(this);
    }

    public String getName(){
    	return name;
    }

    public Room getRoom() {
        return room;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void turn(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

	public void move(Direction movementDirection) throws InvalidMovementException {
		Room newRoom = room.getConnection(movementDirection);

		if (newRoom == null) {
			throw new InvalidMovementException();
		}
		else {
			room.removePlayer(this);
			newRoom.addPlayer(this);
			room = newRoom;
		}
	}

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