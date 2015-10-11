package game;

import game.storage.GameData;
import org.jdom2.Document;

import java.util.List;

public class Furniture extends Item {
	public Furniture(String id, String name, String description, String spriteName) {
		super(id, name, description, spriteName);
	}

	@Override
	public void toXML(Document gameDoc) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object loadXML(GameData gameData) {
		// TODO Auto-generated method stub
		return null;
	}
}
