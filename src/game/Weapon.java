package game;

import org.jdom2.Element;

public class Weapon extends Item implements Pickable {

	public Weapon(String id, String name, String description, String spriteName){
		super(id, name, description,spriteName);
	}

	@Override
	public Element toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		// TODO Auto-generated method stub
	}
}
