package game.storage;

public interface Serializable {
	String toXML();
	void loadXML(String xml);
}
