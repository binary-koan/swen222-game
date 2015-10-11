package game;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Door extends Item {

	private Room targetRoom;

	public Door(String id, String name, String description, String spriteName) {
		super(id, name, description, spriteName);
	}

	public Room getTargetRoom() {
		return targetRoom;
	}

	public void setTargetRoom(Room targetRoom) {
		this.targetRoom = targetRoom;
	}

	@Override
	public Element toXML() {
		Element door = super.toXML();
		door.addContent("targetRoom").setText(this.targetRoom.getID());
		return door;
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);
		this.targetRoom = game.getRoom(objectElement.getChildText("targetRoom"));
	}
}
