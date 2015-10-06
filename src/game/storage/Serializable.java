package game.storage;

import game.Drawable;
import game.Game;

public interface Serializable {
	void toXML();
	Object loadXML(GameData gameData);
}
