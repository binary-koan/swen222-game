package game;

import java.util.*;

import org.jdom2.Element;

public class ControlPanel extends Item {

	public ControlPanel(String id, String name, String description, String spriteName) {
		super(id, name, description, spriteName);
	}

	public void interact(Player player){
		//player.winGame();
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
