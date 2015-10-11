package game;

import org.jdom2.Element;

public class Monster extends Item{

	public Monster(String id, String name, String description, String spriteName) {
		super(id, name, description, spriteName);
	}

	public void attack(){

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
