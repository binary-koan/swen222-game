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
	public void toXML(Document gameDoc) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object loadXML(GameData gameData) {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("resources/mainGame.xml");
		try{
			Document document = builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			for(Element gameRoom : rootNode.getChild("gameItems").getChildren()){
				if(gameRoom.getChildText("id").equals(this.getID())){
					//this.targetRoom = gameData.getRoom(gameRoom.getChild("targetRoom").getText());
				}
			}
			return null;
		}catch (IOException io) {
			System.out.println(io.getMessage());
		}catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return null;
	}
}
