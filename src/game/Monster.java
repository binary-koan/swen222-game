package game;

import org.jdom2.Element;

public class Monster extends Item {
	private String soundEffect;

	public Monster(String id, String name, String description, String spriteName, Weapon weapon) {
		super(id, name, description, spriteName);
		//TODO something with weapon
	}

	public String getSoundEffect() {
		return soundEffect;
	}

	public void attack() {
	}

	@Override
	public Element toXML() {
		return super.toXML();
	}

	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);

		soundEffect = objectElement.getChildText("soundEffect");
	}

	public boolean fight(Player player) {
		return false; //TODO
	}
}
