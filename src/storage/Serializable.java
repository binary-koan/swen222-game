package storage;

import org.jdom2.Element;

import game.Drawable;
import game.Game;

public interface Serializable {

	/**
     *Author: Scott Holdaway
     * Creates an XML element of the object by reading through all the fields
	 * of the object. Subclasses will call this method and add their specific
	 * data to it.
	 * @return The element to be added to xml.
	 */
	Element toXML();

	/**
     * Author: Scott Holdaway
     * Sets all the fields in this object based on an XML element of this object.
     * Subclasses will call this method and add their specific data to it.
	 * @param game The game to check for data associations.
	 * @param objectElement The element to read from.
	 */
	void loadXML(Game game, Element objectElement);
}
