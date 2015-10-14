package game;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Key extends Item implements Pickable {
	public Key(String id, String name, String description, String spriteName) {
		super(id, name, description, spriteName);
	}
}
