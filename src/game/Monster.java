package game;

import org.jdom2.Element;

public class Monster extends Item{

	public Monster(String id, String name, String description, String spriteName, Weapon weapon) {
		super(id, name, description, spriteName);
		//TODO something with weapon
	}

	public void attack(){

	}

	@Override
	public Element toXML() {
		return super.toXML();
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);
	}

	public boolean fight(Player player) {
		return false; //TODO
	}
}
