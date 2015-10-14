package game;

import org.jdom2.Element;

/**
 * Weapon must be help by player to defeat AI's in the game
 * @author Shanon & Scott
 *
 */
public class Weapon extends Holdable {
	public Weapon(String id, String name, String description, String spriteName) {
		super(id, name, description,spriteName);
	}

	/**
	 * {@inheritDoc}
	 * @author holdawscot
	 */
	@Override
	public Element toXML() {
		return super.toXML();
	}

	/**
	 * {@inheritDoc}
	 * @author holdawscot
	 */
	@Override
	public void loadXML(Game game, Element objectElement) {
		super.loadXML(game, objectElement);
	}
}
