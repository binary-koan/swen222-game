package game.storage;

import org.jdom2.Element;

import game.Drawable;
import game.Game;

public interface Serializable {
	Element toXML();
	void loadXML(Game game, Element objectElement);
}
