package game.storage;

import org.jdom2.Document;

import game.Drawable;
import game.Game;

public interface Serializable {
	void toXML(Document gameDoc);
	Object loadXML(GameData gameData);
}
