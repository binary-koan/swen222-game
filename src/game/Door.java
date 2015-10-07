package game;

import game.storage.GameData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Door extends Item {

	private Room targetRoom;

	public Door(String name, String description, String spriteName) {
		super(name, description, spriteName);
	}

	public Room getTargetRoom() {
		return targetRoom;
	}

	public void setTargetRoom(Room targetRoom) {
		this.targetRoom = targetRoom;
	}

	@Override
	public void toXML() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object loadXML(GameData gameData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Action> getAllowedActions() {
		return null; //TODO
	}

	@Override
	public void performAction(Action action, Player player) {
		//TODO
	}
}
