package game;

import game.storage.GameData;

public class Weapon extends Item implements Pickable {

	public Weapon(String name, String description, String spriteName){
		super(name, description,spriteName);
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
