package game;

import game.storage.GameData;

import java.util.List;

public class Key extends Item implements Pickable {
	public Key(String name, String description, String spriteName) {
		super(name, description, spriteName);
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
}
