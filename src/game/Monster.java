package game;

import org.jdom2.Document;

import game.storage.GameData;

public class Monster extends Item{

	public Monster(String id, String name, String description, String spriteName) {
		super(id, name, description, spriteName);
	}

	public void attack(){

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
