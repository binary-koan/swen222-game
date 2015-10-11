package game;

import org.jdom2.Element;

public class Weapon extends Item implements Pickable {

	public Weapon(String id, String name, String description, String spriteName){
		super(id, name, description,spriteName);
	}

	@Override
	public Element toXML() {
		return super.toXML();
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);
	}
}
